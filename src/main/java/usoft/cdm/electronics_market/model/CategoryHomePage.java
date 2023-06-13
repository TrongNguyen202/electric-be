package usoft.cdm.electronics_market.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usoft.cdm.electronics_market.entities.Category;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryHomePage {

    private Integer id;
    private String name;
    private String slug;
    private String picCategory;
    private List<ProductForHomePage> productsDTOS;
    private List<CategoryChildHomePage> categoryList;

    public CategoryHomePage(Integer id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }
}
