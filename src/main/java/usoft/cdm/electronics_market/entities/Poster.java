package usoft.cdm.electronics_market.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdm_poster")
public class Poster {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String title;
    private String note;
    private String img;
    private String idProduct;
    private String idPosterRelated;
    private String content;
    private Date postDate;
    private Integer status = 0;
    @JsonIgnore
    private Integer type;
    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private Date createdDate;
    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;
    @Column(name = "updated_date")
    @LastModifiedDate
    private Date updatedDate;

    public Poster(Integer id, String name, String title, String note, String img, String idProduct, String idPosterRelated, String content, Date postDate, Integer type) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.note = note;
        this.img = img;
        this.idProduct = idProduct;
        this.idPosterRelated = idPosterRelated;
        this.content = content;
        this.postDate =postDate;
        this.type = type;
    }
}
