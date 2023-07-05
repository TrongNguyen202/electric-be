package usoft.cdm.electronics_market.model.Poster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import usoft.cdm.electronics_market.entities.PosterCategory;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosterCategoryModel {
    private Integer id;
    private String name;
    private List<PosterCategory> child;
}
