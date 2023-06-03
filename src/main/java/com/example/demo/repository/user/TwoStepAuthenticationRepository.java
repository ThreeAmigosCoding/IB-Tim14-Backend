package com.example.demo.repository.user;

import com.example.demo.model.user.TwoStepAuthentication;
import com.example.demo.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwoStepAuthenticationRepository extends JpaRepository<TwoStepAuthentication, Integer> {

    Optional<TwoStepAuthentication> findByUserAndCode(User user, Integer code);

}
