package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPromotionDTO {

    private Integer id;
    private Integer promotionId;
    private Integer productId;
    private String name;
    private String brandName;
    private String description;
    private Double priceSell;
    private Double priceAfterSale;
    private String img;

}
