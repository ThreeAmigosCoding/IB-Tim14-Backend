package com.example.demo.service.user;

import com.example.demo.dto.ErrorDTO;
import com.example.demo.dto.user.GoogleOAuthRequestDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.model.user.User;
import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Random;

import static com.example.demo.util.MyCredentials.oAuthClientId;

@Service
public class OauthServiceImpl implements OauthService {

    private static final Random RANDOM = new SecureRandom();
    private final String UPPER_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String NUMBERS = "0123456789";
    private final String SPECIAL_CHARACTERS = "!@#$&*";
    private final String ALL_CHARACTERS = UPPER_ALPHABETS + NUMBERS + SPECIAL_CHARACTERS;

    private static final Logger logger = LoggerFactory.getLogger(OauthServiceImpl.class);

    @Autowired
    private UserService userService;

    @Override
    public User oauthVerification(GoogleOAuthRequestDTO googleOAuthRequest) throws Exception {
        String idTokenString = googleOAuthRequest.getIdToken();
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(oAuthClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            IdToken.Payload payload = idToken.getPayload();

            String email = (String) payload.get("email");
            String name = (String) payload.get("given_name");
            String surname = (String) payload.get("family_name");
            String password = generatePassword(10);

            User existingUser = userService.findByEmail(email);
            if (existingUser != null){
                logger.info("User {} is already in the system", existingUser.getId());
                return existingUser;
            }

            UserDTO userDTO = new UserDTO(name, surname, "+381255855", email,
                    "Default address 123", password);
            existingUser = userService.createNew(userDTO);
            logger.info("New user {} created", existingUser.getId());
            return existingUser;
        } else {
            throw new Exception("Invalid ID token");
        }

    }

    private String generatePassword(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        returnValue.append(UPPER_ALPHABETS.charAt(RANDOM.nextInt(UPPER_ALPHABETS.length()))); // Add an uppercase character
        returnValue.append(SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length()))); // Add a special character
        returnValue.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length()))); // Add a number

        for (int i = 3; i < length; i++) { // Remaining characters
            returnValue.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
        }

        return new String(returnValue);
    }
}
