package usoft.cdm.electronics_market.model.Poster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosterList {
    private Integer id;
    private String name;
    private String title;
    private Date postDate;
    private String createBy;
    private Integer status;
}
