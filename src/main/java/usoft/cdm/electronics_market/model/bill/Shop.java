package usoft.cdm.electronics_market.model.bill;

import lombok.Data;

import java.util.List;

@Data
public class Shop {
    private String code;
    private Double transportFee;
    private String paymentMethod;
    private Double price;
    private Double totalPrice;
    private String note;
    private String phone;
    private String email;
    private String fullname;
    List<Cart> cart;
}
