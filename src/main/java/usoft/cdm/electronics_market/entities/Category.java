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
@Table(name = "cdm_category")
public class Category extends BaseEntity {
    private String name;
    private Integer parentId;
    private String img;

}
