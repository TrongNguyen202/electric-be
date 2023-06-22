package usoft.cdm.electronics_market.model.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    private String code;
    private Double transportFee;
    @NotEmpty(message = "Phương thức thanh toán không được rỗng")
    private String paymentMethod;
    private Double price;
    private Double totalPrice;
    private String note;
    @NotEmpty(message = "Số điện thoại không được rỗng")
    private String phone;
    @NotEmpty(message = "Email không được rỗng")
    private String email;
    private Boolean requestBill;
    private String taxCode;
    private String company;
    private String taxAddress;
    @NotEmpty(message = "Tên không được rỗng")
    private String fullname;
    private String addressTransfer;
    private List<Cart> cart;
    private Integer voucherId;
}
