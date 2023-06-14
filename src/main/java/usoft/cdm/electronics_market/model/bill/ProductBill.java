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
    private Double priceAfterSell;
    private String img;

    public ProductBill(Integer id, Integer billId, String code, String name, Integer quantity, Double priceSell, Double priceAfterSell) {
        this.id = id;
        this.billId = billId;
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.priceSell = priceSell;
        this.priceAfterSell = priceAfterSell;
    }
}
