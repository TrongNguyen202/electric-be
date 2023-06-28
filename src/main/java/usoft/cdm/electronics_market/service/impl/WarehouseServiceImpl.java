package usoft.cdm.electronics_market.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.ProductWarehouse;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.entities.Warehouse;
import usoft.cdm.electronics_market.model.WarehouseDTO;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.repository.ProductWarehouseRepository;
import usoft.cdm.electronics_market.repository.WarehouseRepository;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.service.WarehouseService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    private final UserService userService;

    private final ProductRepository productRepository;

    private final ProductWarehouseRepository productWarehouseRepository;

    @Override
    public Page<WarehouseDTO> getAllWarehouse(Pageable pageable) {
        Page<Warehouse> warehousePage = this.warehouseRepository.findAllByStatus(true, pageable);
        Page<WarehouseDTO> warehouseDTOPage = MapperUtil.mapEntityPageIntoDtoPage(warehousePage, WarehouseDTO.class);
        return warehouseDTOPage;
    }

    @Override
    public Page<WarehouseDTO> search(String name, Pageable pageable) {
        Page<Warehouse> warehousePage = this.warehouseRepository.findAllByStatusAndName(true, name, pageable);
        return MapperUtil.mapEntityPageIntoDtoPage(warehousePage, WarehouseDTO.class);
    }

    @Override
    public ResponseEntity<?> getAllWarehouseList() {
        return ResponseUtil.ok(this.warehouseRepository.findAllByStatus(true));
    }

    @Override
    public ResponseEntity<?> getByWarehouseId(Integer warehouseId) {
        Optional<Warehouse> optionalWarehouse = this.warehouseRepository.findById(warehouseId);
        if (optionalWarehouse.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id của kho");
        }
        WarehouseDTO dto = MapperUtil.map(optionalWarehouse.get(), WarehouseDTO.class);
        return ResponseUtil.ok(dto);
    }

    @Override
    public ResponseEntity<?> save(WarehouseDTO warehouseDTO) {
        Users userLogin = this.userService.getCurrentUser();
        if (!warehouseDTO.getPhone().matches("^(0|(84)|(\\+84))+\\d{9,10}$"))
            return ResponseUtil.badRequest("Số điện thoại không đúng định dạng!");
        Optional<Warehouse> check = this.warehouseRepository.findAllByStatusAndName(true, warehouseDTO.getName());
        if (check.isPresent())
            return ResponseUtil.badRequest("Tên đại lý sản phẩm đã tồn tại");
        if (!isLatitude(warehouseDTO.getLat())) {
            throw new BadRequestException("Vị trí kinh độ không đúng");
        }
        if (!isLongitude(warehouseDTO.getLng())) {
            throw new BadRequestException("Vị trí vĩ độ không đúng");
        }
        Warehouse warehouse = Warehouse
                .builder()
                .name(warehouseDTO.getName())
                .district(warehouseDTO.getDistrict())
                .addressDetail(warehouseDTO.getAddressDetail())
                .ward(warehouseDTO.getWard())
                .city(warehouseDTO.getCity())
                .phone(warehouseDTO.getPhone())
                .lat(warehouseDTO.getLat())
                .lng(warehouseDTO.getLng())
                .status(true)
                .build();
        warehouse.setCreatedBy(userLogin.getUsername());
        this.warehouseRepository.save(warehouse);
        return ResponseUtil.ok(warehouse);
    }

    @Override
    public ResponseEntity<?> update(WarehouseDTO warehouseDTO) {
        Users userLogin = this.userService.getCurrentUser();
        if (!warehouseDTO.getPhone().matches("^(0|(84)|(\\+84))+\\d{9,10}$"))
            return ResponseUtil.badRequest("Số điện thoại không đúng định dạng!");
        if (!isLatitude(warehouseDTO.getLat())) {
            throw new BadRequestException("Vị trí kinh độ không đúng");
        }
        if (!isLongitude(warehouseDTO.getLng())) {
            throw new BadRequestException("Vị trí vĩ độ không đúng");
        }
        Optional<Warehouse> optionalWarehouse = this.warehouseRepository.findById(warehouseDTO.getId());
        if (!optionalWarehouse.get().getName().equals(warehouseDTO.getName())) {
            Optional<Warehouse> check = this.warehouseRepository.findAllByStatusAndName(true, warehouseDTO.getName());
            if (check.isPresent())
                return ResponseUtil.badRequest("Tên đại lý sản phẩm đã tồn tại");
        }
        if (optionalWarehouse.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id của kho");
        }
        Warehouse warehouse = MapperUtil.map(warehouseDTO, Warehouse.class);
        warehouse.setStatus(true);
        warehouse.setUpdatedBy(userLogin.getUsername());
        this.warehouseRepository.save(warehouse);
        return ResponseUtil.ok(warehouse);
    }

    @Override
    public ResponseEntity<?> delete(List<Integer> warehouseIds) {
        Users userLogin = this.userService.getCurrentUser();
        List<Warehouse> warehouseList = new ArrayList<>();
        warehouseIds.forEach(warehouseId -> {
            Optional<Warehouse> optionalWarehouse = this.warehouseRepository.findById(warehouseId);
            if (optionalWarehouse.isEmpty()) {
                throw new BadRequestException("Không tìm thấy id của kho");
            }
            Warehouse warehouse = optionalWarehouse.get();
            List<ProductWarehouse> productWarehouses = this.productWarehouseRepository.findAllByStatusAndWarehouseId(warehouseId, true);
            if (productWarehouses.size() > 0) {
                throw new BadRequestException("Đã có sản phẩm không thể xóa ở kho " + warehouse.getName());
            }

            warehouse.setUpdatedBy(userLogin.getUsername());
            warehouse.setStatus(false);
            warehouseList.add(warehouse);
        });
        this.warehouseRepository.saveAll(warehouseList);
        return ResponseUtil.message(Message.REMOVE);
    }

    private Boolean isLatitude(Double lat) {
        return Double.isFinite(lat) && Math.abs(lat) <= 90;
    }

    private Boolean isLongitude(Double lng) {
        return Double.isFinite(lng) && Math.abs(lng) <= 180;
    }
}
