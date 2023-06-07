package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.HotCategoryDTO;
import usoft.cdm.electronics_market.model.SuggestedProductDTO;
import usoft.cdm.electronics_market.service.SuggestedProductService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/suggested-product")
public class SuggestedProductAPI {

    private final SuggestedProductService productService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllSuggestedProduct(Pageable pageable) {

        return ResponseUtil.ok(this.productService.getAll(pageable));
    }

    @GetMapping()
    public ResponseEntity<?> create(@RequestParam Integer productId) {

        return this.productService.save(productId);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam List<Integer> suggestedProductIds) {

        return this.productService.delete(suggestedProductIds);
    }
}
