package usoft.cdm.electronics_market.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleListDTO extends BaseModel {

    private String name;
    private Integer parentId;
    private String slug;
    private Integer type;// 1 tư vấn sp 2 ẩm thực 3 kinh nghiệm hay

}
