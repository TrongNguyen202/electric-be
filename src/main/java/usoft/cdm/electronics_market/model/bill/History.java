package usoft.cdm.electronics_market.model.bill;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class History {
    private Integer id;
    private String code;
    private Integer status;
    private String addressTransfer;
    private String paymentMethod;
    private String fullname;
    private String phone;
    private Double price;
    private Date purchaseDate;
    private List<ProductBill> product;
}
