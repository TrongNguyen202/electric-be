package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.VerifyOTPRequest;

public interface OtpVerifiService {

    ResponseEntity<?> signUp(VerifyOTPRequest request);
}
