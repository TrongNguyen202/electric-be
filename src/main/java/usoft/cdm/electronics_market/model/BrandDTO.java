package usoft.cdm.electronics_market.model;

import lombok.Data;

@Data
public class BrandDTO {
    private Integer id;
    private String name;
    private String type;
    private String img;
    private Integer sumProducts;

    public BrandDTO(Integer id, String name, Integer sumProducts, String img) {
        this.id = id;
        this.name = name;
        this.sumProducts = sumProducts;
        this.img = img;
    }
}
