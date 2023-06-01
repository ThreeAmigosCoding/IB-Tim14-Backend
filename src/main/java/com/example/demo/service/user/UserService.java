package com.example.demo.service.user;

import com.example.demo.dto.user.UserDTO;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserActivation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    User createNew(UserDTO userDTO);

    User save(User user);

    void delete(User user);

    Optional<User> findById(Integer id);

    User activate(UserActivation activation);

    User findByEmail(String email);

    boolean shouldChangePassword(User user);
}
