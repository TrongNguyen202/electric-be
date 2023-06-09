package usoft.cdm.electronics_market.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.entities.EmailOTP;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.repository.EmailOTPRepository;
import usoft.cdm.electronics_market.repository.UserRepository;
import usoft.cdm.electronics_market.service.EmailService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    private final EmailOTPRepository emailOTPRepository;

    private final UserRepository userRepository;

    @Override
    public void sendEmail(String to, String title, String body) {
        MimeMessage message = this.emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        try {
            message.setFrom("noreply@nextsol.vn");
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(body);
            mimeMessageHelper.setTo(to);
            this.emailSender.send(message);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    @Override
    public ResponseEntity<?> sendGmailForSignUp(String email) {
        Optional<Users> usersOptional = this.userRepository.findByEmailAndStatus(email, true);
        if (usersOptional.isPresent()) {
            throw new BadRequestException("Gmail này đã đăng ký tài khoản rồi,vui lòng đăng nhập");
        }
        Optional<EmailOTP> emailOTP = this.emailOTPRepository.findByEmail(email);
        if (emailOTP.isPresent()) {
            throw new BadRequestException("Gmail này đã đăng ký mã OTP ,vui lòng kiểm tra gmail");
        }
        Integer randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
        this.emailOTPRepository.save(EmailOTP
                .builder()
                .email(email)
                .otp(randomNumber)
                .status(false)
                .build());
        String sub = "Chợ điện máy xác định mã OTP ";
        StringBuilder text = new StringBuilder();
        text.append("Tên đăng nhập: " + email);
        text.append("\n Mật khẩu: " + randomNumber);
        sendEmail(email, sub, text.toString());
        return ResponseUtil.ok("Thành công");
    }

    @Override
    public ResponseEntity<?> checkOTPForSignUp(String email, Integer otp) {
        Optional<EmailOTP> emailOTP = this.emailOTPRepository.findByEmailAndOtp(email, otp);
        if (emailOTP.isEmpty()) {
            throw new BadRequestException("Bạn nhập sai mã OTP ,vui lòng kiểm tra lại gmail để nhập lại");
        }
        this.userRepository
                .save(Users
                        .builder()
                        .roleId(3)
                        .email(email)
                        .status(true)
                        .username(email)
                        .fullname(email)
                        .build());
        return null;
    }
}
