package com.soprasteria.fitbit.repository;

import com.soprasteria.fitbit.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(String userId);

    Page<User> findByDepartament(String departament, Pageable pageable);

}
