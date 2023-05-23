package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.BrandDTO;
import usoft.cdm.electronics_market.service.BrandService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/branch")
public class BrandAPI {
    private final BrandService brandService;

    @GetMapping
    private ResponseEntity<?> getAll(){
        return brandService.getAll();
    }

    @GetMapping("page")
    private ResponseEntity<?> getPage(Pageable pageable){
        return brandService.getPage(pageable);
    }

    @PostMapping
    private ResponseEntity<?> save(@RequestBody BrandDTO dto){
        return brandService.save(dto);
    }

    @DeleteMapping
    private ResponseEntity<?> deleteAll(@RequestParam List<Integer> ids){
        return brandService.remove(ids);
    }
}
