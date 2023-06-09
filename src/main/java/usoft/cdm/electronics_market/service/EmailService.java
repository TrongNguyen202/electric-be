package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;

public interface EmailService {
    void sendEmail(String to, String title, String body) throws MessagingException;

    ResponseEntity<?> sendGmailForSignUp(String email);

    ResponseEntity<?> checkOTPForSignUp(String email, Integer otp);
}
