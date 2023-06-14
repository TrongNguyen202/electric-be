package usoft.cdm.electronics_market.model.bill;

import lombok.Data;

import java.util.List;

@Data
public class History {
    private Integer id;
    private String code;
    private Integer status;
    private String addressTransfer;
    private String paymentMethod;
    private List<ProductBill> product;
}
