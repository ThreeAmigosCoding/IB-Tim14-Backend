package com.example.demo.service.email;

import com.example.demo.util.MyCredentials;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendConfirmationEmail(String to, Integer activationId) {
        Email from = new Email("x509certificatemanager@gmail.com");
        String subject = "Account activation";
        Email toEmail = new Email(to);
        Content content = new Content("text/html", "<a href='https://localhost:8443/api/user/activate/"
                + activationId + "' "
                + "onclick='event.preventDefault(); sendRequest(); return false;'>Click here to activate your account</a>");
        Mail mail = new Mail(from, subject, toEmail, content);

        sendEmail(mail);
    }

    public void sendSimpleEmail(String to, String subject, String body) {
        Email from = new Email("x509certificatemanager@gmail.com");
        String mailSubject = subject;
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, mailSubject, toEmail, content);

        sendEmail(mail);
    }

    private void sendEmail(Mail mail) {
        SendGrid sg = new SendGrid(MyCredentials.sendGridKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
