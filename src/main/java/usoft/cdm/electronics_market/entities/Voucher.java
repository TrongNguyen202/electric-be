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
    private String code;
    private Date startDate;
    private Date endDate;
    private Double discount;
    private Double discountMoney;
    private Integer quantity;
    private Integer quantityUsed;
    private String note;
    private String brand;
    private Boolean status;

    public Voucher(Integer id, String code, Double discount, Double discountMoney){
        this.id = id;
        this.code = code;
        this.discount = discount != null ? discount : discountMoney;
    }
}
