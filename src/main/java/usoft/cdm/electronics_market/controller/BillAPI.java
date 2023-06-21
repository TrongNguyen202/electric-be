package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.bill.Cart;
import usoft.cdm.electronics_market.model.bill.Shop;
import usoft.cdm.electronics_market.service.BillService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.mail.MessagingException;
import javax.validation.Valid;
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

    @GetMapping("getHistory")
    private ResponseEntity<?> getHistory(@RequestParam Integer status) {
        return billService.getHistory(status);
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
    private ResponseEntity<?> shop(@Valid @RequestBody Shop shop) {
        return billService.shop(shop);
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

    @PostMapping("approve")
    private ResponseEntity<?> approve(@RequestParam Integer id, @RequestParam @Nullable Double transferFee, @RequestParam @Nullable String note, @RequestParam @Nullable Integer status) {
        return billService.approve(id, note, transferFee, status);
    }
}
