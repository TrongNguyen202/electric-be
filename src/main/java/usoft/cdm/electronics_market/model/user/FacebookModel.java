package usoft.cdm.electronics_market.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacebookModel {
    private UserFacebook user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserFacebook{
        private String uid;
        private String displayName;
        private String photoURL;
    }
}
