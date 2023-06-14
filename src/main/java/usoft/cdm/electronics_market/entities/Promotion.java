package usoft.cdm.electronics_market.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdm_promotion")
public class Promotion extends BaseEntity {

    private String name;
    private String bannerImg;
    private ZonedDateTime endSale;
    private String description;
    private String content;
    private Boolean status;
}
