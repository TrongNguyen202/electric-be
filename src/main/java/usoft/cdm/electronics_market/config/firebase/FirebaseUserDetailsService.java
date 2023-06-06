//package usoft.cdm.electronics_market.config.firebase;
//
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.TimeUnit;
//
//
//@Component
//public class FirebaseUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private FirebaseApp firebaseApp;
//
//    @Override
//    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
//        try {
//            String verificationId = FirebaseAuth.getInstance(firebaseApp).verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS).getVerificationId();
//            return new FirebaseUserDetails(phoneNumber, verificationId);
//        } catch (FirebaseAuthException e) {
//            throw new UsernameNotFoundException("Could not verify phone number", e);
//        }
//    }
//
//
//}
