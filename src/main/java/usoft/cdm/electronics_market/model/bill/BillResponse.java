package usoft.cdm.electronics_market.model.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.entities.Voucher;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    private String code;
    private Double transportFee;
    private String paymentMethod;
    private Double price;
    private Double totalPrice;
    private String note;
    private String phone;
    private String email;
    private String fullname;
    private String addressTransfer;
    private Boolean requestBill;
    private String taxCode;
    private String company;
    private String taxAddress;
    private Integer status;
    private List<ProductBill> product;
    private List<Voucher> voucher;

    public BillResponse(String code, Double transportFee, String paymentMethod, Double price, Double totalPrice,
                        String note, String phone, String email, String fullname, String addressTransfer, Integer status,
                        Boolean requestBill, String taxCode, String company, String taxAddress) {
        this.code = code;
        this.transportFee = transportFee;
        this.paymentMethod = paymentMethod;
        this.price = price;
        this.totalPrice = totalPrice;
        this.note = note;
        this.phone = phone;
        this.email = email;
        this.fullname = fullname;
        this.addressTransfer = addressTransfer;
        this.status = status;
        this.taxAddress = taxAddress;
        this.taxCode = taxCode;
        this.company = company;
        this.requestBill = requestBill;
    }
}
