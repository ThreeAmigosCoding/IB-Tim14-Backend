package com.example.demo.controller.user;


import com.example.demo.dto.user.UserDTO;
import com.example.demo.model.user.User;
import com.example.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO){
        User user = userService.createNew(userDTO);
        //add validation
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping(value = "/activate")
    public ResponseEntity<?> activateAccount(){
        //service here
        return new ResponseEntity<>("activation works", HttpStatus.OK);
    }
}
