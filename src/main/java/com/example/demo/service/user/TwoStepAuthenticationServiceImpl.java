package com.example.demo.service.user;

import com.example.demo.dto.user.UserTokenState;
import com.example.demo.model.user.TwoStepAuthentication;
import com.example.demo.model.user.User;
import com.example.demo.repository.user.TwoStepAuthenticationRepository;
import com.example.demo.service.email.EmailService;
import com.example.demo.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
public class TwoStepAuthenticationServiceImpl implements TwoStepAuthenticationService {

    @Autowired
    private TwoStepAuthenticationRepository twoStepAuthenticationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserService userService;

    @Override
    public void generateAuthentication(User user) {
        TwoStepAuthentication twoStepAuthentication = new TwoStepAuthentication();
        twoStepAuthentication.setUser(user);
        twoStepAuthentication.setExpirationDate(LocalDate.now().plusDays(1));

        Integer code = generateCode();
        twoStepAuthentication.setCode(code);

        twoStepAuthenticationRepository.save(twoStepAuthentication);

        emailService.sendSimpleEmail(user.getEmail(),
                "Two-step authentication", "Your authentication code is: " + code);
    }

    @Override
    public UserTokenState authenticate(String email, Integer code) throws Exception {
        User user = userService.findByEmail(email);
        if (user == null) throw new Exception("User doesn't exist!");

        TwoStepAuthentication twoStepAuthentication = twoStepAuthenticationRepository.findByUserAndCode(user, code)
                .orElseThrow(() -> new Exception("Invalid code!"));

        if (LocalDate.now().isAfter(twoStepAuthentication.getExpirationDate()))
            throw new Exception("Your authentication code has expired!");

        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        return new UserTokenState(jwt, expiresIn);
    }

    private Integer generateCode() {
        Random random = new Random();
        return random.nextInt(9000) + 1000;
    }

}
