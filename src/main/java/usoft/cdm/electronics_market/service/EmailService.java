package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;

public interface EmailService {
    void sendEmail(String to, String title, String body);

    ResponseEntity<?> sendGmailForSignUp(String email);
}
