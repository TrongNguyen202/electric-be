package usoft.cdm.electronics_market.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO extends BaseModel {
    @NotEmpty(message = "Tên không được rỗng")
    private String name;

    private String type;

    @NotEmpty(message = "Thông tin không được rỗng")
    private String information;

    @NotEmpty(message = "Ảnh không được rỗng")
    private String img;
    private Integer sumProducts;

    private List<ImageDTO> imageDTOS;

    public BrandDTO(Integer id, String name, Integer sumProducts, String img) {
        this.id = id;
        this.name = name;
        this.sumProducts = sumProducts;
        this.img = img;
    }
}
