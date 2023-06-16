package com.example.demo.service.user;

import com.example.demo.dto.user.GoogleOAuthRequestDTO;
import com.example.demo.model.user.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public interface OauthService {
    User oauthVerification(GoogleOAuthRequestDTO googleOAuthRequest) throws Exception;
}
