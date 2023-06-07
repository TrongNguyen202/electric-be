package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.HotCategoryDTO;
import usoft.cdm.electronics_market.service.HotCategoryService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/hot-category")
public class HotCategoryAPI {

    private final HotCategoryService hotCategoryService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllHotCategory(Pageable pageable) {

        return ResponseUtil.ok(this.hotCategoryService.findAll(pageable));
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody HotCategoryDTO dto) {

        return this.hotCategoryService.save(dto);
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody HotCategoryDTO dto) {
        return this.hotCategoryService.update(dto);
    }

    @GetMapping()
    public ResponseEntity<?> getByHotCategoryId(@RequestParam Integer hotCategoryId) {
        return this.hotCategoryService.getById(hotCategoryId);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteHotCategory(@RequestParam List<Integer> hotCategoryIds) {

        return this.hotCategoryService.delete(hotCategoryIds);
    }

}
