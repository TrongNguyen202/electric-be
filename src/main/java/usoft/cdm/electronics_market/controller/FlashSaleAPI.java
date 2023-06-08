package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.FlashSaleDTO;
import usoft.cdm.electronics_market.service.FlashSaleService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/flash-sale")
public class FlashSaleAPI {

    private final FlashSaleService flashSaleService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllFlashSale(Pageable pageable) {

        return ResponseUtil.ok(this.flashSaleService.getAll(pageable));
    }

    @GetMapping()
    public ResponseEntity<?> getById(@RequestParam Integer flashSaleId) {

        return this.flashSaleService.getById(flashSaleId);
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody FlashSaleDTO dto) {

        return this.flashSaleService.save(dto);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody FlashSaleDTO dto) {

        return this.flashSaleService.update(dto);
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@RequestParam List<Integer> flashSaleIds) {

        return this.flashSaleService.delete(flashSaleIds);
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
}
