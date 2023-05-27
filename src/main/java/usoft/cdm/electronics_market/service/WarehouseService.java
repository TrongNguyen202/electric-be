package usoft.cdm.electronics_market.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.WarehouseDTO;

import java.util.List;

public interface WarehouseService {

    Page<WarehouseDTO> getAllWarehouse(Pageable pageable);

    ResponseEntity<?> getAllWarehouseList();

    ResponseEntity<?> getByWarehouseId(Integer warehouseId);

    ResponseEntity<?> save(WarehouseDTO warehouseDTO);

    ResponseEntity<?> update(WarehouseDTO warehouseDTO);

    ResponseEntity<?> delete(List<Integer> warehouseIds);
}
