package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.ProductsDTO;

public interface ProductService {

    ResponseEntity<?> save(ProductsDTO dto);
}
