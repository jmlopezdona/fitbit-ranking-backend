package com.soprasteria.fitbit.security;

import com.soprasteria.fitbit.model.User;
import com.soprasteria.fitbit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;

import java.util.Map;

@EnableOAuth2Sso
@EnableWebSecurity
public class Oauth2Adapter extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private UserService userService;

    // TODO: refactoring using this https://github.com/spring-projects/spring-security/issues/4472
    private OAuth2ClientAuthenticationProcessingFilter oauth2SsoFilter() {
        ApplicationContext applicationContext = this.getApplicationContext();
        OAuth2SsoProperties sso = applicationContext.getBean(OAuth2SsoProperties.class);
        OAuth2RestOperations restTemplate = applicationContext.getBean(UserInfoRestTemplateFactory.class)
                .getUserInfoRestTemplate();
        ResourceServerTokenServices tokenServices = applicationContext.getBean(ResourceServerTokenServices.class);
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(sso
                .getLoginPath());
        filter.setRestTemplate(restTemplate);
        filter.setTokenServices(tokenServices);
        filter.setApplicationEventPublisher(applicationContext);
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            if (oauth2ClientContext.getAccessToken() != null) {
                String refreshToken = oauth2ClientContext.getAccessToken().getRefreshToken().getValue();
                String userId = (String) oauth2ClientContext.getAccessToken().getAdditionalInformation().get("user_id");
                Map principal = (Map) authentication.getPrincipal();

                User user = userService.find(userId);

                if (user==null) {
                    user = new User();
                    user.setUserId(userId);
                    user.setName((String) principal.get("fullName"));
                    user.setAvatarUri((String) principal.get("avatar"));
                }

                user.setRefreshToken(refreshToken);
                userService.save(user);
            }

            response.sendRedirect("/");
        });
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(corsFilter(), SessionManagementFilter.class)
            .antMatcher("/**").authorizeRequests()
            .antMatchers("/", "/login**", "/api/user/ranking", "/api/departament/ranking",  "/webjars/**", "/static/**").permitAll()
            .anyRequest().authenticated()
            .and().addFilterBefore(oauth2SsoFilter(), BasicAuthenticationFilter.class);
        http.csrf().disable();
    }

    @Bean
    CORSFilter corsFilter() {
        CORSFilter filter = new CORSFilter();
        return filter;
    }

}
