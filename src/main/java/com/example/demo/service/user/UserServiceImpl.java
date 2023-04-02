package com.example.demo.service.user;

import com.example.demo.dto.user.UserDTO;
import com.example.demo.model.user.User;
import com.example.demo.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createNew(UserDTO userDTO) {
        User user = new User();

        if (userRepository.findByEmail(userDTO.getEmail()) != null){
            return null; //change to throw new ExceptionXXXXX...
        }

        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setAddress(userDTO.getAddress());
        user.setTelephoneNumber(userDTO.getTelephoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
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
