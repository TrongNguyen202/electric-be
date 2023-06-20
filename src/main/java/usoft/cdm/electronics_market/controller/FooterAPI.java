package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.FooterModel;
import usoft.cdm.electronics_market.service.FooterService;

import javax.validation.Valid;
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

    @DeleteMapping("deleteCustomerCare")
    private ResponseEntity<?> deleteCustomerCare(@RequestBody Integer id) {
        return footerService.deleteCustomerCare(id);
    }

    @PostMapping("getSocialNetwork")
    private ResponseEntity<?> getSocialNetwork(@RequestParam Integer idWarehouse) {
        return footerService.getSocialNetwork(idWarehouse);
    }

    @PostMapping("saveSocialNetwork")
    private ResponseEntity<?> saveSocialNetwork(@RequestBody List<FooterModel> model, @RequestParam Integer idWarehouse) {
        return footerService.saveSocialNetwork(model, idWarehouse);
    }

    @GetMapping("/support-all")
    private ResponseEntity<?> getAllSupportMain(Pageable pageable) {
        return footerService.getAllSupportMain(pageable);
    }

    @GetMapping("/support-id")
    private ResponseEntity<?> getByIdSupportMain(@RequestParam Integer idMain) {
        return footerService.getByIdSupportMain(idMain);
    }

    @GetMapping("/support-list-all")
    private ResponseEntity<?> getAllSupportMainList() {
        return footerService.getAllSupportMainList();
    }

    @PostMapping("/support-save")
    private ResponseEntity<?> saveSupportMain(@Valid @RequestBody FooterModel model) {
        return footerService.saveSupportMain(model);
    }

    @PutMapping("/support-update")
    private ResponseEntity<?> updateSupportMain(@Valid @RequestBody FooterModel model) {
        return footerService.updateSupportMain(model);
    }

    @DeleteMapping("/support-delete")
    private ResponseEntity<?> deleteSupportMain(@RequestParam List<Integer> idMains) {
        return footerService.deleteSupportMain(idMains);
    }


}
