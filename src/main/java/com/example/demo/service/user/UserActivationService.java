package com.example.demo.service.user;

import com.example.demo.model.user.User;
import com.example.demo.model.user.UserActivation;
import org.springframework.stereotype.Service;

@Service
public interface UserActivationService {

    UserActivation save(User user);

    UserActivation findById(Integer id);

    Boolean isActivationValid(UserActivation userActivation);

}
