package com.example.demo.service.user;

import com.example.demo.model.user.PasswordReset;
import com.example.demo.model.user.User;
import com.example.demo.repository.user.PasswordResetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetServiceImpl implements PasswordResetService{
    @Autowired
    private PasswordResetRepository passwordResetRepository;

    public PasswordReset findOne(Integer code, User user){ return passwordResetRepository.findByCodeAndUser(code, user); }

    public PasswordReset save(PasswordReset passwordReset) {
        
        return passwordResetRepository.save(passwordReset);
    }
}
