package usoft.cdm.electronics_market.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductForHomePage {
    private Integer id;
    private String name;
    private Double priceSell;
    private Double priceAfterSale;
    private String slug;
    private String brandName;
    private List<String> img;

    public ProductForHomePage(Integer id, String name, Double priceSell, Double priceAfterSale, String slug, String brandName) {
        this.id = id;
        this.name = name;
        this.priceSell = priceSell;
        this.priceAfterSale = priceAfterSale;
        this.slug = slug;
        this.brandName = brandName;
    }
}
