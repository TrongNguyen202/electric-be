package usoft.cdm.electronics_market.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO extends BaseModel {

    private String fullname;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String username;
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

