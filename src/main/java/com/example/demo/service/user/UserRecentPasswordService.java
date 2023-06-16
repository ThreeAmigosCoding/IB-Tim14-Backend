package com.example.demo.service.user;

import com.example.demo.model.user.User;
import com.example.demo.repository.user.UserRecentPasswordRepository;
import org.springframework.stereotype.Service;

@Service
public interface UserRecentPasswordService {

    void addMostRecent(User user);

    boolean checkForRecentPasswords(User user, String newPassword);



}
