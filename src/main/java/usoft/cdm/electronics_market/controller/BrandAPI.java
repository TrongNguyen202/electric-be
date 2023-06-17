package usoft.cdm.electronics_market.controller;

import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.BrandDTO;
import usoft.cdm.electronics_market.model.CategoryDTO;
import usoft.cdm.electronics_market.model.ImageDTO;
import usoft.cdm.electronics_market.service.BrandService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/branch")
public class BrandAPI {
    private final BrandService brandService;

    @GetMapping
    private ResponseEntity<?> getAll(@RequestParam String type) {
        return brandService.getAll(type);
    }

    @GetMapping("getById")
    private ResponseEntity<?> getById(@RequestParam Integer id) {
        return brandService.getById(id);
    }

    @GetMapping("search")
    private ResponseEntity<?> getById(@RequestParam String name, Pageable pageable) {
        return brandService.search(name, pageable);
    }

    @GetMapping("search-hotbrand")
    private ResponseEntity<?> getHotBrand(@RequestParam String name) {
        return brandService.searchForHotBrand(name);
    }

    @GetMapping("/list")
    private ResponseEntity<?> getAllList() {

        return brandService.getAllList();
    }

    @GetMapping("page")
    private ResponseEntity<?> getPage(Pageable pageable) {
        return brandService.getPage(pageable);
    }

    @PostMapping
    private ResponseEntity<?> save(@Valid @RequestBody BrandRequestBody dto) {

        return brandService.save(dto.getBrandDTO(), dto.getImageList());
    }

    @PutMapping
    private ResponseEntity<?> update(@Valid @RequestBody BrandRequestBody dto) {

        return brandService.update(dto.getBrandDTO(), dto.getImageDTOS());
    }

    @DeleteMapping
    private ResponseEntity<?> deleteAll(@RequestParam List<Integer> ids) {
        return brandService.remove(ids);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class BrandRequestBody {
    @Valid
    private BrandDTO brandDTO;
    @Valid
    private List<String> imageList;
    @Valid
    private List<ImageDTO> imageDTOS;

}
