package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.ProductsDTO;

import java.util.List;

public interface ProductService {

    ResponseEntity<?> save(ProductsDTO dto, List<String> imgList);
}
