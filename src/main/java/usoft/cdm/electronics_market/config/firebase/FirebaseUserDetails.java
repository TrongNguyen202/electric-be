//package usoft.cdm.electronics_market.config.firebase;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//
//public class FirebaseUserDetails implements UserDetails {
//    private String phoneNumber;
//    private String verificationId;
//
//    public FirebaseUserDetails(String phoneNumber, String verificationId) {
//        this.phoneNumber = phoneNumber;
//        this.verificationId = verificationId;
//    }
//
//    public String getVerificationId() {
//        return verificationId;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return new ArrayList<>();
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return phoneNumber;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
