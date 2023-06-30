package usoft.cdm.electronics_market.model;

import lombok.Data;

@Data
public class ProductWarehouseDTO {

    private Integer id;
    private Integer productId;

    private Integer warehouseId;

    private String warehoueName;
}
