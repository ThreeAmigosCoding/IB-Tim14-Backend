package com.example.demo.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, Integer activationId){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("x509certificatemanager@gmail.com");
            helper.setTo(to);
            helper.setSubject("Account activation");
            helper.setText("<a href='http://localhost:8080/api/user/activate/" + activationId+ "' " +
                    "onclick='event.preventDefault(); sendRequest(); return false;'>Click here to activate your account</a>", true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendSimpleEmail(String to, String subject, String body){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("x509certificatemanager@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
