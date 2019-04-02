package com.soprasteria.fitbit.service;

import com.soprasteria.fitbit.model.*;
import com.soprasteria.fitbit.repository.UserRepository;
import com.soprasteria.fitbit.util.RequestResponseLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository repository;

    @Value("${security.oauth2.client.clientId}")
    String fitbitClientId;

    @Value("${security.oauth2.client.clientSecret}")
    String fitbitClientSecret;

    @Value("${security.oauth2.client.accessTokenUri}")
    String fitbitAccessTokenUri;

    @Value("${fitbit.api.resource.activitiesUri}")
    String fitbitActivitiesUri;

    @Value("${fitbit.api.resource.actvitiesStepsLastWeekBaseDateUri}")
    String fitbitActvitiesStepsLastWeekBaseDateUri;

    @Autowired
    RestTemplate restTemplate;

    private final EntityManager em;

    @Autowired
    public UserServiceImpl(JpaContext context) {
        this.em = context.getEntityManagerByManagedType(User.class);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void save(User user) {
        repository.saveAndFlush(user);
    }

    @Override
    public User find(String userId) {
        return repository.findByUserId(userId);
    }

    public String getAccessToken(String userId) {
        User user = find(userId);
        String currentRefreshToken = user.getRefreshToken();
        logger.debug("Current refreshtoken = {}", currentRefreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((fitbitClientId + ":" + fitbitClientSecret).getBytes()));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", currentRefreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RefreshToken response = restTemplate.postForObject(fitbitAccessTokenUri, request, RefreshToken.class);
        String newRefreshToken = response.getRefreshToken();

        logger.debug("New refreshtoken = {}", newRefreshToken);

        user.setRefreshToken(newRefreshToken);
        save(user);

        return response.getAccessToken();
    }

    @Override
    public LifetimeActivity getFitbitLifetime(String userId) {

        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + getAccessToken(userId));

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<Activity> response = restTemplate.exchange(fitbitActivitiesUri, HttpMethod.GET, entity, Activity.class);

        return response.getBody().getLifetime().getTotal();
    }

    @Override
    public ActivitySteps getActivitiesStepsWeekBaseDate(String userId, String date) {

        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + getAccessToken(userId));

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String uri = String.format(fitbitActvitiesStepsLastWeekBaseDateUri, date);

        ResponseEntity<ActivitySteps> response = restTemplate.exchange(uri, HttpMethod.GET, entity, ActivitySteps.class);

        return response.getBody();
    }

    @Override
    public List<User> getRankingWeek(String ranking) throws Exception {
        PageRequest request;

        if (ranking.equals(UserServiceImpl.PREVIOUS_WEEK)) {
            request = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "previusSteps"));
        }
        else if (ranking.equals(UserServiceImpl.CURRENT_WEEK)) {
            request = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "currentSteps"));
        } else {
            throw new Exception("Ranking unknown");
        }

        Page<User> users = findAll(request);
        Iterator<User> itUsers= users.getContent().iterator();

        int position = 1;
        while (itUsers.hasNext()) {
            User user = itUsers.next();
            user.setPosition(position);
            position++;
        }

        return users.getContent();
    }

    @Override
    public List<User> getRanking() throws Exception {
        List<User> rankingPreviousWeek = getRankingWeek(UserService.PREVIOUS_WEEK);
        List<User> rankingCurrentWeek = getRankingWeek(UserService.CURRENT_WEEK);

        Iterator<User> itRankingCurrentWeek = rankingCurrentWeek.iterator();
        while (itRankingCurrentWeek.hasNext()) {
            User userCurrentWeek = itRankingCurrentWeek.next();
            Iterator<User> itUserRankingPreviousWeek = rankingPreviousWeek.iterator();
            int previousPosition = 0;
            while (itUserRankingPreviousWeek.hasNext()) {
                previousPosition++;
                User userPreviousWeek = itUserRankingPreviousWeek.next();
                if (userPreviousWeek.getUserId().equals(userCurrentWeek.getUserId())) {
                    int currentPosition = userCurrentWeek.getPosition();
                    if (currentPosition < previousPosition) {
                        logger.debug("RANKING - set >");
                        userCurrentWeek.setTrend(">");
                    } else if (currentPosition > previousPosition) {
                        logger.debug("RANKING - set <");
                        userCurrentWeek.setTrend("<");
                    } else if (currentPosition == previousPosition) {
                        logger.debug("RANKING - set =");
                        userCurrentWeek.setTrend("=");
                    }
                }
            }
        }

        return rankingCurrentWeek;
    }
}