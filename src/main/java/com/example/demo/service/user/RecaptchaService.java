package com.example.demo.service.user;

import org.springframework.stereotype.Service;

@Service
public interface RecaptchaService {
    public boolean verifyRecaptcha(String response);
}
