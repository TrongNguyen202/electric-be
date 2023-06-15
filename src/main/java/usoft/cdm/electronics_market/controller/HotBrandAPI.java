package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.HotBrandDTO;
import usoft.cdm.electronics_market.service.HotBrandService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/hot-brand")
public class HotBrandAPI {

    private final HotBrandService hotBrandService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody HotBrandDTO dto) {
        return this.hotBrandService.save(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllHotBrand(Pageable pageable) {
        return ResponseUtil.ok(this.hotBrandService.findAll(pageable));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteHotBrand(@RequestParam List<Integer> hotBrandIds) {

        return this.hotBrandService.delete(hotBrandIds);
    }


}
