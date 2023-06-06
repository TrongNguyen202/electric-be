//package usoft.cdm.electronics_market.config.firebase;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.FirebaseToken;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//
//public class FirebaseAuthenticationProvider implements AuthenticationProvider {
//
//    @Autowired
//    private FirebaseConfig firebaseApp;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        FirebaseToken token = null;
//        try {
//            token = FirebaseAuth.getInstance(firebaseApp).verifyIdToken((String) authentication.getCredentials());
//        } catch (FirebaseAuthException e) {
//            throw new BadCredentialsException("Could not authenticate Firebase token", e);
//        }
//        UserDetails userDetails = new User(token.getUid(), "", new ArrayList<>());
//
//        return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return (FirebaseAuthenticationToken.class.isAssignableFrom(authentication);
//    }{
//}
