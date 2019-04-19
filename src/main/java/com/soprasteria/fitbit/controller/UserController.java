package com.soprasteria.fitbit.controller;

import com.soprasteria.fitbit.model.Departament;
import com.soprasteria.fitbit.model.User;
import com.soprasteria.fitbit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @RequestMapping("/api/user/departament/users/ranking")
    public List<User> getRankingByUserInDepartament(OAuth2Authentication authentication) throws Exception {
        Map principal = (Map) authentication.getPrincipal();
        User user = userService.find((String) principal.get("encodedId"));
        return userService.getRankingByUserInDepartament(user.getDepartament());
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
