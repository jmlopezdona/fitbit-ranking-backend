package com.soprasteria.fitbit.controller;

import com.soprasteria.fitbit.model.LifetimeActivity;
import com.soprasteria.fitbit.model.User;
import com.soprasteria.fitbit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private UserService userService;

    @GetMapping("/api/ranking")
    public List<User> getRanking() throws Exception {
        return userService.getRanking();
    }

    @GetMapping("/api/ranking/current")
    public List<User> getRankingCurrentWeek() throws Exception {
        return userService.getRankingWeek(UserService.CURRENT_WEEK);
    }

    @GetMapping("/api/ranking/previous")
    public List<User> getRankingPreviousWeek() throws Exception {
        return userService.getRankingWeek(UserService.PREVIOUS_WEEK);
    }

    @RequestMapping("/api/lifetime-activity")
    public LifetimeActivity lifetimeActivity() {
        return userService.getFitbitLifetime((String)oauth2ClientContext.getAccessToken().getAdditionalInformation().get("user_id"));
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

}
