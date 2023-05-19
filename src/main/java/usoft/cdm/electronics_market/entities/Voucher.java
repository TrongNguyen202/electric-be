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
@Table(name = "cdm_voucher")
public class Voucher extends BaseEntity {

    private String name;
    private String code;
    private Date timeUsing;
    private String type;
    private Double discount;
    private Integer quantity;

}
