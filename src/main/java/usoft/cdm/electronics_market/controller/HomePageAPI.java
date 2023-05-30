package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.service.CategoryService;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.util.ResponseUtil;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/homepage")
public class HomePageAPI {

    private final ProductService productService;

    private final CategoryService categoryService;

    @GetMapping("/product-category")
    public ResponseEntity<?> getAllProductAndCategoryForHome() {
        return this.productService.getAllProductAndCategoryForHome();
    }

    @PostMapping("/product")
    public ResponseEntity<?> getAllProductFromCategoryId(@RequestParam Integer categoryId, @RequestBody ProductsDTO dto, Pageable pageable) {
        return ResponseUtil.ok(this.productService.getAllProductFromCategoryId(categoryId, pageable, dto));
    }

    @GetMapping("/category")
    public ResponseEntity<?> findByAllForParentIdNull() {
        return this.categoryService.findByAllForParentIdNull();
    }

    @GetMapping("/category-child")
    public ResponseEntity<?> getAllCategoryChild(@RequestParam Integer categoryId, Pageable pageable) {
        return this.categoryService.displayCategoryChild(categoryId, pageable);
    }

    @GetMapping("/related-product")
    public ResponseEntity<?> getRelatedProducts(@RequestParam Integer productId) {
        return this.productService.getRelatedProducts(productId);
    }


    @GetMapping("/same-brand")
    public ResponseEntity<?> getProductsInSameBrand(@RequestParam Integer productId) {
        return this.productService.getProductsInSameBrand(productId);
    }
}
