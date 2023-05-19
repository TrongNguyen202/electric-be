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
@Table(name = "cdm_bill")
public class Bill extends BaseEntity {

    private String code;
    private Double transportFee;
    private String paymentMethod;
    private Double totalPrice;
    private String note;
    private Integer customerId;
    private Integer informationReceiverId;

}
