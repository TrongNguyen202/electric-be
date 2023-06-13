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
    private Double price;
    private Double totalPrice;
    private String note;
    private Integer userId;
    private String phone;
    private String email;
    private String fullname;
    private Boolean requestBill;
    private String taxCode;
    private String company;
    private String taxAddress;
    private Integer status;  //cart 1 waiting 2
    private String addressTransfer;
}
