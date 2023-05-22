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
@Table(name = "cdm_bill_voucher")
public class BillVoucher extends BaseEntity{

    private Integer voucherId;
    private Integer billId;
    private Boolean status;

}
