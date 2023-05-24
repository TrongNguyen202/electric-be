package usoft.cdm.electronics_market.controller;


import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.model.TitleAttributeDTO;
import usoft.cdm.electronics_market.service.ProductService;

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


    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequestBody request) {

        return this.productService.save(request.getProductsDTO(), request.getImg(), request.getDto());
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody ProductRequestBody request) {

        return this.productService.update(request.getProductsDTO(), request.getImg(), request.getDto());
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
