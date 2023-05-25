package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private ResponseEntity<?> getAll(){
        return billService.getAll();
    }

    @PostMapping
    private ResponseEntity<?> addCartToBill(@RequestBody List<Cart> list){
        return billService.addCartToBill(list);
    }

    @GetMapping("getCart")
    private ResponseEntity<?> getCart(){
        return billService.getCart();
    }

    @PostMapping("shop")
    private ResponseEntity<?> shop(@RequestBody Shop shop){
        return billService.shop(shop);
    }
}
