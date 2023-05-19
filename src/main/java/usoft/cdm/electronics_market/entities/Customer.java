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
@Table(name = "cdm_customer")

public class Customer extends BaseEntity {

    private Integer addressId;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String addressDetail;
    private String type;
    private String description;
}
