package com.example.demo.service.user;

import com.example.demo.dto.user.UserDTO;
import com.example.demo.model.user.Role;
import com.example.demo.model.user.User;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User createNew(UserDTO userDTO) {
        User user = new User();

        if (userRepository.findByEmail(userDTO.getEmail()) != null){
            return null; //change to throw new ExceptionXXXXX...
        }

        List<Role> roles = new ArrayList<>(Collections.singletonList(roleService.findByName("ROLE_USER")));

        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setAddress(userDTO.getAddress());
        user.setTelephoneNumber(userDTO.getTelephoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(roles);
        //maybe implement mapper
        return save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
}
