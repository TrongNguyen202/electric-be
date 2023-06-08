package usoft.cdm.electronics_market.model;

import lombok.Data;

@Data
public class FooterModel {
    private Integer id;
    private String name;
    private String content;

    public FooterModel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
