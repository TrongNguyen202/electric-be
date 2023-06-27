package usoft.cdm.electronics_market.model.voucher;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class VoucherModel {
    private Integer id;
    private String code;
    private Date startDate;
    private Date endDate;
    private Double discount;
    private Double discountMoney;
    private Integer quantity;
    private Integer quantityUsed;
    private String note;
    private List<Integer> brandId;

    public VoucherModel(Integer id, String code, Date startDate, Date endDate, Double discount, Double discountMoney, Integer quantity, Integer quantityUsed, String note, String brand) {
        this.id = id;
        this.code = code;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
        this.discountMoney = discountMoney;
        this.quantity = quantity;
        this.quantityUsed = quantityUsed;
        this.note = note;
        String[] branchList = brand.split("\\|\\|");
        for (String s : branchList) {
            this.brandId.add(Integer.parseInt(s));
        }
    }
}
