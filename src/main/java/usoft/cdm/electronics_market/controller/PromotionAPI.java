package usoft.cdm.electronics_market.controller;


import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.ProductPromotionDTO;
import usoft.cdm.electronics_market.model.PromotionDTO;
import usoft.cdm.electronics_market.service.PromotionService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotion")
public class PromotionAPI {

    private final PromotionService promotionService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPromotion(Pageable pageable) {

        return ResponseUtil.ok(this.promotionService.findAll(pageable));
    }

    @GetMapping()
    public ResponseEntity<?> getById(@RequestParam Integer promotionId) {

        return this.promotionService.getById(promotionId);
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody PromotionRequestBody dto) {

        return this.promotionService.save(dto.getPromotionDTO(), dto.getProductPromotionDTOS());
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody PromotionRequestBody dto) {

        return this.promotionService.update(dto.getPromotionDTO(), dto.getProductPromotionDTOS());
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@RequestParam List<Integer> promotionIds) {

        return this.promotionService.delete(promotionIds);
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class PromotionRequestBody {
    @Valid
    private PromotionDTO promotionDTO;
    private List<ProductPromotionDTO> productPromotionDTOS;
}