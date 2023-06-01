package com.example.demo.service.user;

import com.example.demo.model.user.User;
import com.example.demo.model.user.UserRecentPassword;
import com.example.demo.repository.user.UserRecentPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserRecentPasswordServiceImpl implements UserRecentPasswordService{

    @Autowired
    private UserRecentPasswordRepository userRecentPasswordRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void addMostRecent(User user) {
        UserRecentPassword userRecentPassword = new UserRecentPassword();
        userRecentPassword.setUser(user);
        userRecentPassword.setReplacedPassword(user.getPassword());
        userRecentPassword.setReplaceDate(LocalDate.now());

        List<UserRecentPassword> userRecentPasswords = userRecentPasswordRepository
                .findByUserOrderByReplaceDateDesc(user);

        if (userRecentPasswords.size() >= 5)
            userRecentPasswordRepository.delete(userRecentPasswords.get(userRecentPasswords.size() - 1));

        userRecentPasswordRepository.save(userRecentPassword);


    }

    @Override
    public boolean checkForRecentPasswords(User user, String newPassword) {
        List<UserRecentPassword> userRecentPasswords = userRecentPasswordRepository
                .findByUserOrderByReplaceDateDesc(user);

        for (UserRecentPassword userRecentPassword : userRecentPasswords){
            if (bCryptPasswordEncoder.matches(newPassword, userRecentPassword.getReplacedPassword()) ||
                    bCryptPasswordEncoder.matches(newPassword, user.getPassword())  ) return true;
        }

        return false;
    }
}
