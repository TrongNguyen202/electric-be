package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotBrandDTO extends BaseModel {
    private Integer brandId;
    private String imgBrand;
    private String name;
}
