package com.example.demo.controller.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "api/user")
public class UserController {


    @PostMapping(value = "/register")
    public ResponseEntity<?> register(){
        //service here
        return new ResponseEntity<>("registration works", HttpStatus.OK);
    }


    @PostMapping(value = "/activate")
    public ResponseEntity<?> activateAccount(){
        //service here
        return new ResponseEntity<>("activation works", HttpStatus.OK);
    }
}
