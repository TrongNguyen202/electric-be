package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.CapacityModel;
import usoft.cdm.electronics_market.service.CapacityService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/capacity")
public class CapacityAPI {
    private final CapacityService capacityService;

    @GetMapping
    private ResponseEntity<?> getAll(){
        return capacityService.getAll();
    }

    @GetMapping("page")
    private ResponseEntity<?> getPage(Pageable pageable){
        return capacityService.getPage(pageable);
    }

    @PostMapping
    private ResponseEntity<?> save(@RequestBody CapacityModel dto){
        return capacityService.save(dto);
    }

    @DeleteMapping
    private ResponseEntity<?> deleteAll(@RequestParam List<Integer> ids){
        return capacityService.remove(ids);
    }
}
