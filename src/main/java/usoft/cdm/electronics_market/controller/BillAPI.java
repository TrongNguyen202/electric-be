package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.bill.Cart;
import usoft.cdm.electronics_market.model.bill.Shop;
import usoft.cdm.electronics_market.service.BillService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/bill")
public class BillAPI {
    private final BillService billService;

    @GetMapping
    private ResponseEntity<?> getAll(@RequestParam @Nullable Integer status, Pageable pageable) {
        return billService.getAll(status, pageable);
    }

    @GetMapping("getById")
    private ResponseEntity<?> getById(@RequestParam Integer id) {
        return billService.getById(id);
    }

    @PostMapping
    private ResponseEntity<?> addCartToBill(@RequestBody List<Cart> list) {
        return billService.addCartToBill(list);
    }

    @GetMapping("getCart")
    private ResponseEntity<?> getCart() {
        return billService.getCart();
    }

    @PostMapping("shop")
    private ResponseEntity<?> shop(@RequestBody Shop shop) {
        return billService.shop(shop);
    }

    @PostMapping("approve")
    private ResponseEntity<?> approve(@RequestParam Integer id, @RequestParam @Nullable Double transferFee, @RequestParam @Nullable String note, @RequestParam @Nullable Integer status) {
        return billService.approve(id, note, transferFee, status);
    }
}
