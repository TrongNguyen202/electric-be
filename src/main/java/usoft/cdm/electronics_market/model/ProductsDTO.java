package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    private Integer warehouseId;
    private Double priceSell;
    private Double priceImport;
    private Date dateSaleStart;
    private Date dateSaleEnd;
    private String information;
    private Double priceAfterSale;
    private String slug;
    private Integer quantity;
    private Integer reviewId;
    private List<String> img;
    private List<TitleAttributeDTO> dto;

}
