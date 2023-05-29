package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.bill.Cart;
import usoft.cdm.electronics_market.model.bill.Shop;

import java.util.List;

public interface BillService {
    ResponseEntity<?> getAll(Integer status, Pageable pageable);

    ResponseEntity<?> getById(Integer id);

    ResponseEntity<?> addCartToBill(List<Cart> cart);

    ResponseEntity<?> shop(Shop shop);

    ResponseEntity<?> getCart();

    ResponseEntity<?> approve(Integer id, String note, Double transferFee, Integer status);
}
