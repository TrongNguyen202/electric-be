package usoft.cdm.electronics_market.model;

import lombok.Data;

@Data
public class FooterModel {
    private Integer id;
    private String name;
    private String content;
    private String link;

    public FooterModel(Integer id, String name, String link) {
        this.id = id;
        this.name = name;
        this.link = link;
    }
}
