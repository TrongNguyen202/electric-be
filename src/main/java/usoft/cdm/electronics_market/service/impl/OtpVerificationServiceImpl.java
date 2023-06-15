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
import usoft.cdm.electronics_market.config.security.CustomUserDetails;
import usoft.cdm.electronics_market.config.security.JwtTokenProvider;
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

    private final JwtTokenProvider jwtTokenProvider;

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

    @Override
    public ResponseEntity<?> checkPhoneForgetPassword(String phone) {
        Optional<Users> usersOptional = this.userRepository.findByUsernameAndStatus(phone, true);
        if (usersOptional.isEmpty()) {
            throw new BadRequestException("Số điện thoại này chưa được đăng ký ");
        }
        return ResponseUtil.ok("Kiểm tra thành công");
    }

    @Override
    public ResponseEntity<?> getForgetPassword(VerifyOTPRequest request) {
        String verificationId = request.getVerificationId();
        try {
            FirebaseToken token = firebaseAuth.verifyIdToken(verificationId);
            String phoneNumber = token.getClaims().get("phone_number").toString();
            String phoneMain = phoneNumber.replace("+84", "0");
            Optional<Users> usersOptional = this.userRepository.findByPhoneAndStatus(phoneMain, true);
            if (usersOptional.isEmpty()) {
                throw new BadRequestException("Chưa đăng ký tài khoản vs số điện thoại này rồi");
            }
            Users users = usersOptional.get();
            users.setPassword(this.passwordEncoder.encode(request.getOtp()));
            this.userRepository.save(users);
            return ResponseUtil.ok(this.jwtTokenProvider.generateToken(new CustomUserDetails(users)));
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return ResponseUtil.badRequest("Xác thực OTP sai");
        }
    }

    @Override
    public ResponseEntity<?> changePassFromPhone(String token) {
        Integer idToken = this.jwtTokenProvider.getUserIdFromJWT(token);
        Optional<Users> usersOptional = this.userRepository.findByIdAndStatus(idToken, true);

        return null;
    }

}
