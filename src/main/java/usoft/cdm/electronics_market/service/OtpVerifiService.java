package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.VerifyOTPRequest;
import usoft.cdm.electronics_market.model.user.ChangePassword;

public interface OtpVerifiService {

    ResponseEntity<?> signUp(VerifyOTPRequest request);

    ResponseEntity<?> checkPhoneForgetPassword(String phone);

    ResponseEntity<?> getForgetPassword(VerifyOTPRequest request);

    ResponseEntity<?> changePassFromPhone(String token, ChangePassword dto);
}
