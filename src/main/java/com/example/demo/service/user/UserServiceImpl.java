package com.example.demo.service.user;

import com.example.demo.dto.user.UserDTO;
import com.example.demo.model.user.Role;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserActivation;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

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
        user.setActive(false);
        user.setLastPasswordResetDate(new Timestamp((new Date()).getTime()));
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

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean shouldChangePassword(User user) {
        Timestamp today = new Timestamp((new Date()).getTime());
        System.out.println(today + " -> " + user.getLastPasswordResetDate());
        long millisecondsDifference = Math.abs(user.getLastPasswordResetDate().getTime() - today.getTime());
        long minutesDifference = millisecondsDifference / (60 * 1000);
        System.out.println(minutesDifference);

        return minutesDifference > 2;
    }

    @Override
    public User activate(UserActivation activation) {
        User user = userRepository.findByEmail(activation.getUser().getEmail());
        user.setActive(true);
        return userRepository.save(user);
    }
}
