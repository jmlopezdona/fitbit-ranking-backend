package com.soprasteria.fitbit.repository;

import com.soprasteria.fitbit.model.UserSteps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStepsRepository extends JpaRepository<UserSteps, Long> {

}
