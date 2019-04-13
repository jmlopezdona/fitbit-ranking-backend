package com.soprasteria.fitbit.service;

import com.soprasteria.fitbit.model.UserSteps;
import com.soprasteria.fitbit.repository.UserStepsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStepsServiceImpl implements  UserStepsService {

    @Autowired
    private UserStepsRepository repository;

    @Override
    public void save(UserSteps userSteps) {
        repository.saveAndFlush(userSteps);
    }

}