package usoft.cdm.electronics_market.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FooterModel {

    private Integer id;
    @NotEmpty(message = "Tên không được rỗng")
    private String name;

    @NotEmpty(message = "Tên không được rỗng")
    private String content;
    private String link;

    @NotEmpty(message = "Ảnh không được rỗng")
    private String icon;

    public FooterModel(Integer id, String name, String link) {
        this.id = id;
        this.name = name;
        this.link = link;
    }
}
