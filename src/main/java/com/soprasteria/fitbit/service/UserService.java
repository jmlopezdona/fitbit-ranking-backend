package com.soprasteria.fitbit.service;

import com.soprasteria.fitbit.model.ActivitySteps;
import com.soprasteria.fitbit.model.LifetimeActivity;
import com.soprasteria.fitbit.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    public static final String PREVIOUS_WEEK = "PREVIOUS_WEEK";
    public static final String CURRENT_WEEK = "CURRENT_WEEK";

    Page<User> findAll(Pageable pageable);

    void save(User user);

    User find(String userId);

    String getAccessToken(String userId);

    LifetimeActivity getFitbitLifetime(String userId);

    ActivitySteps getActivitiesStepsWeekBaseDate(String userId, String date);

    List<User> getRankingWeek(String ranking) throws Exception;

    List<User> getRanking() throws Exception;

}
