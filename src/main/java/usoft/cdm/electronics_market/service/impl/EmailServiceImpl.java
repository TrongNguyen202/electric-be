package usoft.cdm.electronics_market.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.entities.EmailOTP;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.EmailDTO;
import usoft.cdm.electronics_market.repository.EmailOTPRepository;
import usoft.cdm.electronics_market.repository.UserRepository;
import usoft.cdm.electronics_market.service.EmailService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    private final EmailOTPRepository emailOTPRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void sendEmail(String to, String title, String body) {
        MimeMessage message = this.emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        try {
            message.setFrom("noreply@nextsol.vn");
            message.setContent(body, "text/html; charset=UTF-8");
            mimeMessageHelper.setSubject(title);
//            mimeMessageHelper.setText(body);
            mimeMessageHelper.setTo(to);
            this.emailSender.send(message);
        } catch (MessagingException messageException) {
            throw new RuntimeException(messageException);
        }
    }

    @Override
    public ResponseEntity<?> sendGmailForSignUp(String email) throws MessagingException {
        Optional<Users> usersOptional = this.userRepository.findByEmailAndStatus(email, true);
        if (usersOptional.isPresent()) {
            throw new BadRequestException("Gmail này đã đăng ký tài khoản rồi,vui lòng đăng nhập");
        }
        Optional<EmailOTP> emailOTP = this.emailOTPRepository.findByEmailAndStatus(email, false);
        if (emailOTP.isPresent()) {
            throw new BadRequestException("Gmail này đã đăng ký mã OTP ,vui lòng kiểm tra gmail");
        }
        Integer randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
        EmailOTP otp = this.emailOTPRepository.save(EmailOTP
                .builder()
                .email(email)
                .otp(randomNumber)
                .status(false)
                .build());
        Instant instant = otp.getCreatedDate().toInstant();
        LocalDateTime localDateTimeCreateDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        otp.setExpiryTime(localDateTimeCreateDate.plusSeconds(60));
        this.emailOTPRepository.save(otp);
        String sub = "Chợ điện máy xác định mã OTP ";
        StringBuilder text = new StringBuilder();
        text.append("Tên đăng nhập: " + email);
        text.append("\n Mật khẩu: " + randomNumber);
        sendEmail(email, sub, text.toString());
        return ResponseUtil.ok("Thành công");
    }

    @Override
    public ResponseEntity<?> checkOTPForSignUp(EmailDTO dto) {
        Optional<EmailOTP> emailOTP = this.emailOTPRepository.findByEmailAndOtp(dto.getEmail(), dto.getOtp());
        if (emailOTP.isEmpty()) {
            throw new BadRequestException("Bạn nhập sai mã OTP ,vui lòng kiểm tra lại gmail để nhập lại");
        }
        Users users = this.userRepository
                .save(Users
                        .builder()
                        .roleId(3)
                        .email(dto.getEmail())
                        .status(true)
                        .username(dto.getEmail())
                        .fullname(dto.getEmail())
                        .password(this.passwordEncoder.encode(dto.getOtp().toString()))
                        .build());
        emailOTP.get().setStatus(true);
        this.emailOTPRepository.save(emailOTP.get());
        return ResponseUtil.ok(users);
    }

    @Override
    public ResponseEntity<?> sendAgainOTP(String email) {
        Optional<EmailOTP> emailOTP = this.emailOTPRepository.findByEmailAndStatus(email, false);
        if (emailOTP.isPresent()) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (currentDateTime.isBefore(emailOTP.get().getExpiryTime())) {
                throw new BadRequestException("Bạn phải đợi 50s ");
            }
            EmailOTP otp = emailOTP.get();
            Integer randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
            otp.setOtp(randomNumber);
            otp.setExpiryTime(otp.getExpiryTime().plusSeconds(60));
            this.emailOTPRepository.save(otp);
        } else {
            throw new BadRequestException("Chưa có gmail này ");
        }
        return ResponseUtil.ok("Thành công");
    }
}
