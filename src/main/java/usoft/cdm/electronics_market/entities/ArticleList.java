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
@Table(name = "cdm_article_list")
public class ArticleList extends BaseEntity {
    private String name;
    private Boolean status;
    private Integer parentId;
    private String slug;
    private Integer type;// 1 tư vấn sp 2 ẩm thực 3 kinh nghiệm hay

}
