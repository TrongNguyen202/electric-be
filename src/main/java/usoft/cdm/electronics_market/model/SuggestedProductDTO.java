package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestedProductDTO extends BaseModel {
    private Integer productId;
    private String nameCategory;
    private String nameProduct;
    private Double discount;
    private String brandName;
    private Double priceAfterSale;
    private Double priceSell;
    private List<String> img;

    private Integer quantitySell;

}
