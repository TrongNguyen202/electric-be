package usoft.cdm.electronics_market.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdm_flash_sale")
public class FlashSale extends BaseEntity {

    private Integer productId;
    private Integer quantitySale;
    private ZonedDateTime startSale;
    private ZonedDateTime endSale;
    private Double priceFlashSale;
    private String description;
    private Boolean status;
}
