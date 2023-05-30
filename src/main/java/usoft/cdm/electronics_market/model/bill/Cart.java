package usoft.cdm.electronics_market.model.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Integer id;
    private Integer billId;
    private Integer productId;
    private Double priceSell;
    private Integer quantity;
}
