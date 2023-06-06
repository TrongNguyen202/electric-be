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
@Table(name = "cdm_users")
public class Users extends BaseEntity {

    private String fullname;
    private String username;
    private String password;
    private Integer roleId;
    private Integer addressId;
    private String addressDetail;
    private String phone;
    private String email;
    private String type;
    private Boolean status;
    private String description;
    private String avatar;
}
