package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.HomePageDTO;
import usoft.cdm.electronics_market.service.HomePageService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager-homepage")
public class ManagerHomePageAPI {
    private final HomePageService homePageService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllBanner(Pageable pageable) {
        return ResponseUtil.ok(this.homePageService.findByAll(pageable));
    }

    @GetMapping()
    public ResponseEntity<?> getBannerId(@RequestParam Integer homepageIds) {
        return ResponseUtil.ok(this.homePageService.getById(homepageIds));
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

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody HomePageDTO dto) {

        return this.homePageService.save(dto);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody HomePageDTO dto) {

        return this.homePageService.update(dto);
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@RequestParam List<Integer> homepageIds) {

        return this.homePageService.delete(homepageIds);
    }
}
