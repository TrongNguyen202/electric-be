package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.FlashSaleDTO;

import java.util.List;

public interface FlashSaleService {

    ResponseEntity<?> save(FlashSaleDTO dto);

    ResponseEntity<?> update(FlashSaleDTO dto);

    ResponseEntity<?> getById(Integer flashSaleId);

    Page<FlashSaleDTO> getAll(Pageable pageable);

    ResponseEntity<?> delete(List<Integer> flashSaleIds);
}
