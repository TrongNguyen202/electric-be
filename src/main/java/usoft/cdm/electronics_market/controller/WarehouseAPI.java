package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.WarehouseDTO;
import usoft.cdm.electronics_market.service.WarehouseService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseAPI {

    private final WarehouseService warehouseService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllWarehouse(Pageable pageable) {

        return ResponseUtil.ok(this.warehouseService.getAllWarehouse(pageable));
    }

    @GetMapping()
    public ResponseEntity<?> getByWarehouseId(@RequestParam Integer warehouseId) {
        return this.warehouseService.getByWarehouseId(warehouseId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.append(error.getDefaultMessage()).append(",");
        });
        return ResponseUtil.badRequest(errors.toString());
    }


    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody WarehouseDTO dto) {

        return this.warehouseService.save(dto);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody WarehouseDTO dto) {

        return this.warehouseService.update(dto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteWarehouse(@RequestParam List<Integer> warehouseIds) {

        return this.warehouseService.delete(warehouseIds);
    }
}
