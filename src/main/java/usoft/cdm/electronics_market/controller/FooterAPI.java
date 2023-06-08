package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.FooterModel;
import usoft.cdm.electronics_market.service.FooterService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/footer")
public class FooterAPI {
    private final FooterService footerService;

    @GetMapping
    private ResponseEntity<?> getHotline() {
        return footerService.getHotline();
    }

    @PostMapping
    private ResponseEntity<?> getHotline(@RequestParam String hotline) {
        return footerService.saveHotline(hotline);
    }

    @GetMapping
    private ResponseEntity<?> getCustomerCare(Pageable pageable) {
        return footerService.getCustomerCare(pageable);
    }

    @GetMapping
    private ResponseEntity<?> getAllCustomerCare() {
        return footerService.getAllCustomerCare();
    }

    @GetMapping
    private ResponseEntity<?> getHotline(@RequestParam Integer id) {
        return footerService.getCustomerCareById(id);
    }

    @GetMapping
    private ResponseEntity<?> getHotline(@RequestBody FooterModel model) {
        return footerService.saveCustomerCare(model);
    }
}
