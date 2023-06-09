package usoft.cdm.electronics_market.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdm_email_otp")
public class EmailOTP extends BaseEntity {
    private String email;
    private Integer otp;
    private Boolean status; // true đã sign-up success false sign-up not success
}
