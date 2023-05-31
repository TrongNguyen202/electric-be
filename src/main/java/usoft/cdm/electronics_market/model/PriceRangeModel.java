package usoft.cdm.electronics_market.model;

import lombok.Data;

@Data
public class PriceRangeModel {
    private String name;
    private Double priceTo;
    private Double priceFrom;
    private Integer quantity;

    public PriceRangeModel(String name, Double priceFrom, Double priceTo) {
        this.name = name;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
    }
}
