package com.soprasteria.fitbit.service;

import com.soprasteria.fitbit.model.ActivitySteps;
import com.soprasteria.fitbit.model.Departament;
import com.soprasteria.fitbit.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    String PREVIOUS_WEEK = "PREVIOUS_WEEK";
    String CURRENT_WEEK = "CURRENT_WEEK";

    Page<User> findAll(Pageable pageable);

    void save(User user);

    User find(String userId);

    String getAccessToken(String userId);

    ActivitySteps getActivitiesStepsWeekBaseDate(String userId, String date);

    List<Departament> getRankingByDepartament();

    List<User> getRankingByUserInDepartament(String departament) throws Exception;

}
