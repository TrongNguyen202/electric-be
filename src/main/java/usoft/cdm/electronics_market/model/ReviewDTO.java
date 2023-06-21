package usoft.cdm.electronics_market.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usoft.cdm.electronics_market.entities.Image;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO extends BaseModel {
    private Float vote;
    private String content;
    private Integer parentId;
    private Integer userId;
    private Integer productId;
    private List<Image> imageList;
}
