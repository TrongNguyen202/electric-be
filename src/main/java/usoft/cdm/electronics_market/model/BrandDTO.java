package usoft.cdm.electronics_market.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BrandDTO {
    private Integer id;
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
