package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.FooterModel;
import usoft.cdm.electronics_market.service.FooterService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/footer")
public class FooterAPI {
    private final FooterService footerService;

    @GetMapping
    private ResponseEntity<?> getHotline(@RequestParam Integer idWarehouse) {
        return footerService.getHotline(idWarehouse);
    }

    @PostMapping("saveHotline")
    private ResponseEntity<?> saveHotline(@RequestParam Integer idWarehouse, @RequestParam String hotline) {
        return footerService.saveHotline(hotline, idWarehouse);
    }

    @GetMapping("getCustomerCare")
    private ResponseEntity<?> getCustomerCare(Pageable pageable, @RequestParam Integer idWarehouse) {
        return footerService.getCustomerCare(pageable, idWarehouse);
    }

    @GetMapping("getAllCustomerCare")
    private ResponseEntity<?> getAllCustomerCare(@RequestParam Integer idWarehouse) {
        return footerService.getAllCustomerCare(idWarehouse);
    }

    @GetMapping("getCustomerCareById")
    private ResponseEntity<?> getCustomerCareById(@RequestParam Integer id) {
        return footerService.getCustomerCareById(id);
    }

    @PostMapping("saveCustomerCare")
    private ResponseEntity<?> saveCustomerCare(@RequestBody FooterModel model, @RequestParam Integer idWarehouse) {
        return footerService.saveCustomerCare(model, idWarehouse);
    }

    @PostMapping("getSocialNetwork")
    private ResponseEntity<?> getSocialNetwork(@RequestParam Integer idWarehouse) {
        return footerService.getSocialNetwork(idWarehouse);
    }

    @PostMapping("saveSocialNetwork")
    private ResponseEntity<?> saveSocialNetwork(@RequestBody List<FooterModel> model, @RequestParam Integer idWarehouse) {
        return footerService.saveSocialNetwork(model, idWarehouse);
    }
}
