package usoft.cdm.electronics_market.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdm_product_detail")
public class ProductDetail extends BaseEntity {

    private Integer attributeId;
    private Integer productId;
    private String value;
    private Integer reviewId;
    private Integer quantity;
}
