package usoft.cdm.electronics_market.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usoft.cdm.electronics_market.model.BrandDTO;

import javax.persistence.*;


@SqlResultSetMapping(
        name = "getAllBrandByProducts",
        classes = {
                @ConstructorResult(
                        targetClass = BrandDTO.class,
                        columns = {
                                @ColumnResult(name = "id", type = Integer.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "sumProducts", type = Integer.class),
                                @ColumnResult(name = "img", type = String.class),
                        }
                )
        }
)

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdm_brand")
public class Brand extends BaseEntity {
    private String name;
    private String img;
    private String type;
    private String information;
    private Boolean status;

}
