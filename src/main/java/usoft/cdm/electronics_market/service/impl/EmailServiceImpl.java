package usoft.cdm.electronics_market.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.service.EmailService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendEmail(String to, String title, String body) {
        MimeMessage message = this.emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        try {
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(body);
            mimeMessageHelper.setTo(to);
            this.emailSender.send(message);
        } catch (MessagingException messageException) {
            throw new RuntimeException(messageException);
        }
    }

    @Override
    public ResponseEntity<?> sendGmailForSignUp(String email) {
        String sub = "Gmail xác định mã OTP ";
        StringBuilder text = new StringBuilder();
        text.append("Cần xác thực");
        text.append("Có thắc mắc gì vui lòng liên hệ với chi nhánh ");
        sendEmail(email, sub, text.toString());
        return ResponseUtil.ok("Thành công");
    }
}
