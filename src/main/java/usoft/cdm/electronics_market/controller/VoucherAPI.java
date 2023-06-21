package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/voucher")
public class VoucherAPI {


    @GetMapping
    private ResponseEntity<?> getAll(){
        return null;
    }

    @PostMapping
    private ResponseEntity<?> save(){
        return null;
    }
}
