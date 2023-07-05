package usoft.cdm.electronics_market.model.Poster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosterSaveCategory {
    private Integer id;
    private String name;
    private Integer parentId;
}
