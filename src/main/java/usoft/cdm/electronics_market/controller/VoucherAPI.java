package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.voucher.VoucherModel;
import usoft.cdm.electronics_market.service.VoucherService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/voucher")
public class VoucherAPI {
    private final VoucherService voucherService;

    @GetMapping
    private ResponseEntity<?> getAll(Pageable pageable){
        return voucherService.getAll(pageable);
    }

    @GetMapping("getVoucher")
    private ResponseEntity<?> getVoucher(List<Integer> ids){
        return voucherService.getVoucher(ids);
    }

    @GetMapping("getById")
    private ResponseEntity<?> getById(@RequestParam Integer id){
        return voucherService.getById(id);
    }

    @PostMapping
    private ResponseEntity<?> save(@RequestBody VoucherModel model){
        return voucherService.save(model);
    }

    @DeleteMapping
    private ResponseEntity<?> getAll(@RequestParam Integer id){
        return voucherService.delete(id);
    }
}
