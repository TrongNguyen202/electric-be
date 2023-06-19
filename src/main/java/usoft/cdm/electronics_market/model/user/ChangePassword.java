package usoft.cdm.electronics_market.model.user;

import lombok.Data;

@Data
public class ChangePassword {
    private Integer id;
    private String password;
    private String passAgain;
}
