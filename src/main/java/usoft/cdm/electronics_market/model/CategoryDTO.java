package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usoft.cdm.electronics_market.entities.Category;
import usoft.cdm.electronics_market.entities.Products;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO extends BaseModel {

    @NotEmpty(message = "Tên không được rỗng")
    @Size(min = 2, max = 500, message = "Tên phải từ 2 đến 500 ký tự")
    private String name;

    private Integer parentId;

    @NotEmpty(message = "Icon không được rỗng")
    private String iconImg;

    @NotEmpty(message = "Avatar không được rỗng")
    private String avatarImg;

    private String slug;

    private List<Category> categoryList;

    private List<String> imageList;

    private String picCategory;

    private Boolean status;

    private List<ProductsDTO> productsDTOS;

    private List<CategoryDTO> dtos;

    private Integer sumProduct;

    public CategoryDTO(Integer id, String iconImg, String name, String slug, Integer sumProduct) {
        this.id = id;
        this.iconImg = iconImg;
        this.name = name;
        this.slug = slug;
        this.sumProduct = sumProduct;
    }

    public CategoryDTO(Integer id, String name, String slug, Integer sumProduct, Integer parentId) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.sumProduct = sumProduct;
        this.parentId = parentId;
    }

}
