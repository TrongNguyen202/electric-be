package usoft.cdm.electronics_market.controller;


import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.CategoryDTO;
import usoft.cdm.electronics_market.service.CategoryService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryAPI {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(Pageable pageable) {

        return ResponseUtil.ok(this.categoryService.findByAll(pageable));
    }


    @GetMapping("/list")
    public ResponseEntity<?> getAll() {
        return ResponseUtil.ok(this.categoryService.getAllList());
    }

    @GetMapping()
    public ResponseEntity<?> getById(@RequestParam Integer idCategory) {
        return this.categoryService.displayById(idCategory);
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody CategoryRequestBody request) {

        return this.categoryService.save(request.getCategoryDTO(), request.getImageList());
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody CategoryRequestBody request) {

        return this.categoryService.update(request.getCategoryDTO(), request.getImageList());
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

    @PostMapping("/child")
    public ResponseEntity<?> createChild(@Valid @RequestBody CategoryDTO dto) {

        return this.categoryService.saveChildCategory(dto);
    }

    @PutMapping("/child")
    public ResponseEntity<?> updateChild(@Valid @RequestBody CategoryDTO dto) {

        return this.categoryService.updateChildCategory(dto);
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@RequestParam List<Integer> categoryIds) {

        return this.categoryService.deleteCategoryIds(categoryIds);
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CategoryRequestBody {
    @Valid
    private CategoryDTO categoryDTO;
    private List<String> imageList;
}
