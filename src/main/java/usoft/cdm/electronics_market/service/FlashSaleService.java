package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.FlashSaleDTO;

public interface FlashSaleService {

    ResponseEntity<?> save(FlashSaleDTO dto);
}
