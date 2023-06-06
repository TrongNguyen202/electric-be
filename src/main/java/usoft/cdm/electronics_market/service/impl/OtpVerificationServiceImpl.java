package usoft.cdm.electronics_market.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.VerifyOTPRequest;
import usoft.cdm.electronics_market.repository.UserRepository;
import usoft.cdm.electronics_market.service.OtpVerifiService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class OtpVerificationServiceImpl implements OtpVerifiService {

    private final FirebaseAuth firebaseAuth;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> signUp(VerifyOTPRequest request) {
        String verificationId = request.getVerificationId();
        try {
            FirebaseToken token = firebaseAuth.verifyIdToken(verificationId);
            String phoneNumber = token.getClaims().get("phone_number").toString();
            String phoneMain = phoneNumber.replace("+84", "0");
            Optional<Users> usersOptional = this.userRepository.findByPhoneAndStatus(phoneMain, true);
            if (usersOptional.isPresent()) {
                throw new BadRequestException("Đã đăng ký tài khoản vs số điện thoại này rồi");
            }
            Users users = Users
                    .builder()
                    .username(phoneMain)
                    .password(this.passwordEncoder.encode(request.getOtp()))
                    .phone(phoneMain)
                    .roleId(3)
                    .status(true)
                    .build();
            this.userRepository.save(users);
            return ResponseUtil.ok("Sign-up is successful");
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return ResponseUtil.badRequest("Xác thực OTP sai");
        }
    }
}
