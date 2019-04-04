package com.soprasteria.fitbit.controller;

import com.soprasteria.fitbit.model.Departament;
import com.soprasteria.fitbit.model.LifetimeActivity;
import com.soprasteria.fitbit.model.User;
import com.soprasteria.fitbit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private UserService userService;

    @GetMapping("/api/user/ranking")
    public List<User> getRanking() throws Exception {
        return userService.getRanking();
    }

    @GetMapping("/api/user/ranking/current")
    public List<User> getRankingCurrentWeek() throws Exception {
        return userService.getRankingWeek(UserService.CURRENT_WEEK);
    }

    @GetMapping("/api/user/ranking/previous")
    public List<User> getRankingPreviousWeek() throws Exception {
        return userService.getRankingWeek(UserService.PREVIOUS_WEEK);
    }

    @RequestMapping("/api/lifetime-activity")
    public LifetimeActivity lifetimeActivity() {
        return userService.getFitbitLifetime((String)oauth2ClientContext.getAccessToken().getAdditionalInformation().get("user_id"));
    }

    @RequestMapping("/api/departament/ranking")
    public List<Departament> getRankingByDepartament() {
        return userService.getRankingByDepartament();
    }

    @RequestMapping("/api/user")
    public User getUser(OAuth2Authentication authentication) {
        Map principal = (Map) authentication.getPrincipal();
        User user = userService.find((String) principal.get("encodedId"));
        user.setRefreshToken("");
        return user;
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

}
