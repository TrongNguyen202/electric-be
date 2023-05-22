package usoft.cdm.electronics_market.controller;


import lombok.*;
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

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody CategoryRequestBody request) {

        return this.categoryService.save(request.getCategoryDTO(), request.getImageList());
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody CategoryDTO dto) {

        return this.categoryService.update(dto);
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
