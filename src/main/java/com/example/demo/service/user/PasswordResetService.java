package com.example.demo.service.user;

import com.example.demo.model.user.PasswordReset;
import com.example.demo.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface PasswordResetService {

    PasswordReset findOne(Integer code, User user);
    PasswordReset save(PasswordReset passwordReset);
}
