package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomePageDTO extends BaseModel {
    @NotEmpty(message = "Ảnh không được rỗng")
    private String img;

    @NotNull(message = "Loại Banner không được rỗng")
    private Integer type;

    private String linkProduct;


}
