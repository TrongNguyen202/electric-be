package usoft.cdm.electronics_market.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.entities.Warehouse;
import usoft.cdm.electronics_market.model.WarehouseDTO;
import usoft.cdm.electronics_market.repository.WarehouseRepository;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.service.WarehouseService;
import usoft.cdm.electronics_market.util.ResponseUtil;


@Transactional
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    private final UserService userService;

    @Override
    public ResponseEntity<?> save(WarehouseDTO warehouseDTO) {
        Users userLogin = this.userService.getCurrentUser();
        Warehouse warehouse = Warehouse
                .builder()
                .name(warehouseDTO.getName())
                .status(true)
                .build();
        warehouse.setCreatedBy(userLogin.getUsername());
        this.warehouseRepository.save(warehouse);
        return ResponseUtil.ok(warehouse);
    }

    @Override
    public ResponseEntity<?> update(WarehouseDTO warehouseDTO) {
        Users userLogin = this.userService.getCurrentUser();
        return null;
    }
}
