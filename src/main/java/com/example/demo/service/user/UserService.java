package com.example.demo.service.user;

import com.example.demo.dto.user.UserDTO;
import com.example.demo.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User createNew(UserDTO userDTO);

    public User save(User user);

    public void delete(User user);



}
