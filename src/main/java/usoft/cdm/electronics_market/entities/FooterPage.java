package usoft.cdm.electronics_market.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdm_footer_page")
public class FooterPage extends BaseEntity {
    private String name;
    private String icon;
    private String content;
    private String link;
    private Integer idWarehouse;
    @JsonIgnore
    private Integer type;
}
