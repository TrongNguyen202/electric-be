package usoft.cdm.electronics_market.model.bill;

import lombok.Data;

@Data
public class Cart {
    private Integer id;
    private Integer billId;
    private Integer productDetailId;
    private Double priceSell;
    private Integer quantity;
}
