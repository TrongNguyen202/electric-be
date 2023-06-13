package usoft.cdm.electronics_market.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usoft.cdm.electronics_market.model.CategoryChildHomePage;
import usoft.cdm.electronics_market.model.CategoryHomePage;

import javax.persistence.*;


@SqlResultSetMapping(
        name = "getParentIdIsNullAndStatus",
        classes = {
                @ConstructorResult(
                        targetClass = CategoryHomePage.class,
                        columns = {
                                @ColumnResult(name = "id", type = Integer.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "slug", type = String.class),
                        }
                )
        }
)

@SqlResultSetMapping(
        name = "getParentIdAndStatus",
        classes = {
                @ConstructorResult(
                        targetClass = CategoryChildHomePage.class,
                        columns = {
                                @ColumnResult(name = "id", type = Integer.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "slug", type = String.class),
                                @ColumnResult(name = "iconImg", type = String.class),
                                @ColumnResult(name = "avatarImg", type = String.class),
                        }
                )
        }
)

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdm_category")
public class Category extends BaseEntity {

    private String name;
    private Integer parentId;
    private String iconImg;
    private String avatarImg;
    private String slug;
    private Boolean status;

}
