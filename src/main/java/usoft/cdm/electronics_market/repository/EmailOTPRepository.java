package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.EmailOTP;

import java.util.Optional;

@Repository
public interface EmailOTPRepository extends JpaRepository<EmailOTP, Integer> {

    Optional<EmailOTP> findByEmail(String email, Boolean status);

    Optional<EmailOTP> findByEmailAndOtp(String email, Integer otp);
}
