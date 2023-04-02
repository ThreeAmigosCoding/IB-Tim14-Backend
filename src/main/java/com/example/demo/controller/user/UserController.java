package com.example.demo.controller.user;


import com.example.demo.dto.ErrorDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserTokenState;
import com.example.demo.model.user.User;
import com.example.demo.service.user.UserService;
import com.example.demo.util.TokenUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/user")
public class UserController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody UserDTO authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user);
            int expiresIn = tokenUtils.getExpiredIn();

            System.out.println(user.getAuthorities());

            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("Wrong username or password!"), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO){
        User user = userService.createNew(userDTO);

        if (user == null) return new //change to try catch for .crateNew method when exceptions are implemented
                ResponseEntity<>("User with this username already exists!", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping(value = "/activate")
    public ResponseEntity<?> activateAccount(){
        //service here
        return new ResponseEntity<>("activation works", HttpStatus.OK);
    }
}
