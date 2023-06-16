package com.example.demo.repository.user;

import com.example.demo.model.user.PasswordReset;
import com.example.demo.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer> {

    PasswordReset findByCodeAndUser(Integer code, User user);
}
