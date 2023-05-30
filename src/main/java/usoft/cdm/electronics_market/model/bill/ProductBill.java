package usoft.cdm.electronics_market.model.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBill {
    private Integer id;
    private Integer billId;
    private String code;
    private String name;
    private Integer quantity;
    private Double priceSell;
}
