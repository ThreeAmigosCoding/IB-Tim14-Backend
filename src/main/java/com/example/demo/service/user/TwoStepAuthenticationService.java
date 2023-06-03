package com.example.demo.service.user;

import com.example.demo.dto.user.UserTokenState;
import com.example.demo.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface TwoStepAuthenticationService {

    void generateAuthentication(User user);

    UserTokenState authenticate(String email, Integer code) throws Exception;

}
