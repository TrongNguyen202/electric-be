package usoft.cdm.electronics_market.model.Poster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavePoster {
    private Integer id;
    private String name;
    private String title;
    private String note;
    private String img;
    private String idProduct;
    private String idPosterRelated;
    private Date postDate;
    private String content;
    private Integer type;
}
