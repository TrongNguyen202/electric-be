package usoft.cdm.electronics_market.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdm_products")
public class Products extends BaseEntity {
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
    private Boolean status;
}
