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
@Table(name = "cdm_warehouse")
public class Warehouse extends BaseEntity {

    private String name;

    private String addressDetail;

    private String city;
    private String district;
    private String ward;
    private String phone;
    private Double lat;
    private Double lng;
    private Boolean status;
}
