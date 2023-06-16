package com.example.demo.service.user;

import com.example.demo.model.user.User;
import com.example.demo.model.user.UserActivation;
import com.example.demo.repository.user.UserActivationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserActivationServiceImpl implements UserActivationService {

    @Autowired
    private UserActivationRepository userActivationRepository;

    @Override
    public UserActivation save(User user){
        UserActivation userActivation = new UserActivation();
        userActivation.setUser(user);
        userActivation.setDate(new Date());
        userActivation.setLifetime(3);
        return userActivationRepository.save(userActivation);
    }

    @Override
    public UserActivation findById(Integer id) {
        return userActivationRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean isActivationValid(UserActivation userActivation) {
        return (new Date().getTime() - userActivation.getDate().getTime()) / 86400000 <= userActivation.getLifetime();
    }

}
