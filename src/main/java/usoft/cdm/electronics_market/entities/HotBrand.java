package usoft.cdm.electronics_market.entities;

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
@Table(name = "cdm_hot_brand")
public class HotBrand extends BaseEntity {
    private Integer brandId;
    private String imgBrand;
    private String name;
    private String information;

}
