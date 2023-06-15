package com.example.demo.controller.user;


import com.example.demo.dto.ErrorDTO;
import com.example.demo.dto.user.PasswordResetDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.model.user.PasswordReset;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserActivation;
import com.example.demo.service.email.EmailService;
import com.example.demo.service.user.*;
import com.example.demo.util.TokenUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping(value = "api/user")
public class UserController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserActivationService userActivationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRecentPasswordService userRecentPasswordService;

    @Autowired
    private TwoStepAuthenticationService twoStepAuthenticationService;

    @Autowired
    private RecaptchaService recaptchaService;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody UserDTO authenticationRequest, @RequestParam String recaptchaResponse) {
        try {
            if (!recaptchaService.verifyRecaptcha(recaptchaResponse)) {
                return new ResponseEntity<>(new ErrorDTO("ReCaptcha validation failed!"), HttpStatus.BAD_REQUEST);
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();

            if (!user.isActive())
                return new ResponseEntity<>("This account is not active!", HttpStatus.BAD_REQUEST);

            if (user.getLastPasswordResetDate() != null && userService.shouldChangePassword(user))
                return new ResponseEntity<>(new ErrorDTO("Your password expired!"), HttpStatus.BAD_REQUEST);

            twoStepAuthenticationService.generateAuthentication(user);

            return new ResponseEntity<>(new ErrorDTO("An authentication code has been sent to your email."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("Wrong username or password!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/two-step-authentication/{email}/{code}", consumes = "application/json")
    public ResponseEntity<?> twoStepAuthentication(@PathVariable String email, @PathVariable Integer code) {
        try {
            return ResponseEntity.ok(twoStepAuthenticationService.authenticate(email, code));
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, @RequestParam String recaptchaResponse){
        if (!recaptchaService.verifyRecaptcha(recaptchaResponse)) {
            return new ResponseEntity<>(new ErrorDTO("ReCaptcha validation failed!"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNew(userDTO);
        if (user == null) return new //change to try catch for .crateNew method when exceptions are implemented
                ResponseEntity<>("User with this username already exists!", HttpStatus.BAD_REQUEST);

        UserActivation userActivation = userActivationService.save(user);
        emailService.sendConfirmationEmail(user.getEmail(), userActivation.getId());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{activation-code}")
    public ResponseEntity<?> activateAccount(@PathVariable("activation-code") Integer activationId){
        UserActivation userActivation = userActivationService.findById(activationId);
        if (userActivation == null) {
            return new ResponseEntity<>("Activation with entered id does not exist!",
                    HttpStatus.NOT_FOUND);
        }

        if (!userActivationService.isActivationValid(userActivation)){
            emailService.sendSimpleEmail(userActivationService.findById(activationId).getUser().getEmail(),
                    "Activation expired", "Your activation with id=" + activationId + " has expired!");
            return new ResponseEntity<>(new ErrorDTO("Activation expired. Register again!"), HttpStatus.BAD_REQUEST);
        }

        userService.activate(userActivationService.findById(activationId));
        emailService.sendSimpleEmail(userActivationService.findById(activationId).getUser().getEmail(),
                "Account activated", "Your activation with id=" + activationId + " was successful!");
        return new ResponseEntity<>(new ErrorDTO("Successful account activation!"), HttpStatus.OK);
    }

    @GetMapping(value = "/{email}/resetPassword")
    public ResponseEntity<?> getResetRequest(@PathVariable String email){
        User user = userService.findByEmail(email);

        if (user == null) {
            return new ResponseEntity<>("User with this email does not exist!", HttpStatus.NOT_FOUND);
        }

        PasswordReset passwordReset = new PasswordReset(user);
        PasswordReset passwordResetSaved = passwordResetService.save(passwordReset);

        emailService.sendSimpleEmail(user.getEmail(), "Password reset", "To reset password please enter" +
                "this code: " + passwordResetSaved.getCode());
        return new ResponseEntity<>("Email with reset code has been sent!", HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{email}/resetPassword")
    public ResponseEntity<?> resetPassword(@PathVariable String email, @RequestBody PasswordResetDTO passwordResetDTO){
        User user = userService.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);
        }

        PasswordReset passwordReset = passwordResetService.findOne(passwordResetDTO.getCode(), user);
        if (passwordReset == null || passwordReset.getExpiresIn().getTime() < new Date().getTime()){
            return new ResponseEntity<>("Code is expired or not correct!", HttpStatus.BAD_REQUEST);
        }

        //Potentially make this with new reset using old password new password
        if (userRecentPasswordService.checkForRecentPasswords(user, passwordResetDTO.getNewPassword())){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, -5);
            Date fiveDaysAgo = calendar.getTime();
            passwordReset.setExpiresIn(fiveDaysAgo);
            passwordResetService.save(passwordReset);

            return new ResponseEntity<>("You tried to use one of you last 5 passwords!", HttpStatus.BAD_REQUEST);
        }

        userRecentPasswordService.addMostRecent(user);

        user.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));
        user.setLastPasswordResetDate(new Timestamp((new Date()).getTime()));
        userService.save(user);

        return new ResponseEntity<>("Password successfully changed!", HttpStatus.NO_CONTENT);

    }
}
