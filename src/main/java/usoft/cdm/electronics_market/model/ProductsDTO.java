package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductsDTO extends BaseModel {

    private String name;
    private String code;
    private Integer categoryId;
    private Integer brandId;
    private Integer capicityId;
    private Integer addressId;
    private Double priceSell;
    private Double priceImport;
    private Date dateSale;
    private Double priceAfterSale;
    private String information;
    private Integer warehouseId;
    private String slug;

}
