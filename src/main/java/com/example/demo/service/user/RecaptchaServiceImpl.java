package com.example.demo.service.user;

import com.example.demo.dto.user.RecaptchaResponse;
import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class RecaptchaServiceImpl implements RecaptchaService {

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final RestTemplate restTemplate = new RestTemplate();
    private final Gson gson = new Gson();

    @Override
    public boolean verifyRecaptcha(String response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>("response=" + response + "&secret=" + recaptchaSecretKey, headers);
        ResponseEntity<String> recaptchaResponseEntity = restTemplate.postForEntity(RECAPTCHA_VERIFY_URL, entity, String.class);

        if (recaptchaResponseEntity.getStatusCode() == HttpStatus.OK) {
            RecaptchaResponse recaptchaResponse = gson.fromJson(recaptchaResponseEntity.getBody(), RecaptchaResponse.class);
            return recaptchaResponse.isSuccess();
        }

        return false;
    }
}
