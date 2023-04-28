package com.example.demo.repository.user;

import com.example.demo.model.user.UserActivation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserActivationRepository extends JpaRepository<UserActivation, Integer> {

    Optional<UserActivation> findById(Integer id);

}
