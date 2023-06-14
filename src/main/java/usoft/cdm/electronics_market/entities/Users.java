package usoft.cdm.electronics_market.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String password;
    private Integer roleId;
    private Integer addressId;
    private String addressDetail;
    private String phone;
    private String email;
    private String birthday;
    private String sex;
    private Boolean status;
    private String description;
    private String avatar;
}
