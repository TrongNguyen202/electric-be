package usoft.cdm.electronics_market.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class OtpVerificationService {
    private final FirebaseAuth firebaseAuth;

    public OtpVerificationService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

//    public boolean verifyOtp(String phoneNumber, String otp) {
//        try {
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneNumber, otp);
//            FirebaseAuth.getInstance().signInWithCredential(credential);
//            return true;
//        } catch (FirebaseAuthException e) {
//            // Xử lý lỗi xác thực OTP
//            return false;
//        }
//    }
}
