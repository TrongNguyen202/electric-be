package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.bill.Cart;
import usoft.cdm.electronics_market.model.bill.Shop;

import java.util.List;

public interface BillService {
    ResponseEntity<?> getAll();

    ResponseEntity<?> addCartToBill(List<Cart> cart);

    ResponseEntity<?> shop(Shop shop);

    ResponseEntity<?> getCart();
}
