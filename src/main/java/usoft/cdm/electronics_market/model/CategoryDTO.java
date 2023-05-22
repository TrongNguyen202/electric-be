package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usoft.cdm.electronics_market.config.expection.CheckImg;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO extends BaseModel {

    @NotEmpty(message = "Tên không được rỗng")
    @Size(min = 6, max = 500, message = "Tên phải từ 6 đến 500 ký tự")
    private String name;

    private Integer parentId;

    @NotEmpty(message = "Icon không được rỗng")
    private String iconImg;

    @NotEmpty(message = "Avatar không được rỗng")
    private String avatarImg;
}
