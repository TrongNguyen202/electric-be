package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotCategoryDTO extends BaseModel {
    private Integer categoryId;
    private Double discount;
    private String nameCategory;
}
