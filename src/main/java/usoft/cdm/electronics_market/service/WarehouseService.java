package usoft.cdm.electronics_market.service;


import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.WarehouseDTO;

public interface WarehouseService {
    ResponseEntity<?> save(WarehouseDTO warehouseDTO);

    ResponseEntity<?> update(WarehouseDTO warehouseDTO);
}
