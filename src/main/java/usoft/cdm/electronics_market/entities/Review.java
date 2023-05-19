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
@Table(name = "cdm_review")
public class Review extends BaseEntity {
    private Integer customerId;
    private Float vote;
    private String content;
    private Integer parentId;
    private Integer userId;

}
