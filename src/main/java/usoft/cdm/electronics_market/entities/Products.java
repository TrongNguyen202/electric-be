package usoft.cdm.electronics_market.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usoft.cdm.electronics_market.model.ProductsDTO;

import javax.persistence.*;
import java.util.Date;

@SqlResultSetMapping(
        name = "getAllMadeInProducts",
        classes = {
                @ConstructorResult(
                        targetClass = ProductsDTO.class,
                        columns = {
                                @ColumnResult(name = "id", type = Integer.class),
                                @ColumnResult(name = "madeIn", type = String.class),
                                @ColumnResult(name = "sumProducts", type = Integer.class)
                        }
                )
        }
)

@SqlResultSetMapping(
        name = "getBrandAndPriceAndMadeIn",
        classes = {
                @ConstructorResult(
                        targetClass = ProductsDTO.class,
                        columns = {
                                @ColumnResult(name = "id", type = Integer.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "priceSell", type = Double.class),
                                @ColumnResult(name = "priceAfterSale", type = Double.class),
                                @ColumnResult(name = "slug", type = String.class),
                        }
                )
        }
)


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
    private String madeIn;
}
