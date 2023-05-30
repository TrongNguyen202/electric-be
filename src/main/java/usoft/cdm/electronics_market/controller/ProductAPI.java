package usoft.cdm.electronics_market.controller;


import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.model.TitleAttributeDTO;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductAPI {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllProduct(Pageable pageable) {
        return this.productService.getAllProducts(pageable);
    }

    @GetMapping()
    public ResponseEntity<?> getByProductId(@RequestParam Integer productId) {
        return this.productService.getByProductId(productId);
    }

    @GetMapping("search-category")
    public ResponseEntity<?> searchByCategory(@RequestParam(required = false) Integer categoryId, Pageable pageable) {
        return ResponseUtil.ok(this.productService.searchByCategory(categoryId, pageable));
    }

    @GetMapping("search-name")
    public ResponseEntity<?> searchByName(@RequestParam(required = false) String name, Pageable pageable) {
        return ResponseUtil.ok(this.productService.searchByName(name, pageable));
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
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequestBody request) {

        return this.productService.save(request.getProductsDTO(), request.getImg(), request.getDto());
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody ProductRequestBody request) {

        return this.productService.update(request.getProductsDTO(), request.getImg(), request.getDto());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProducts(@RequestParam List<Integer> productIds) {

        return this.productService.deleteProductByIds(productIds);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ProductRequestBody {
    @Valid
    private ProductsDTO productsDTO;
    private List<String> img;
    private List<TitleAttributeDTO> dto;
}
