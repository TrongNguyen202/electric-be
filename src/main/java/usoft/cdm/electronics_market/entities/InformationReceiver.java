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
@Table(name = "cdm_information_receicer")
public class InformationReceiver extends BaseEntity {
    private Integer addressId;

    private String name;
    private String phone;
    private String email;
    private String addressDetail;
    private String description;


}
