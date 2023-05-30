package usoft.cdm.electronics_market.model.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private List<Cart> cart;
    private List<Integer> voucherId;
}
