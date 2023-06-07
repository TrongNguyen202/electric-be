package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.SuggestedProductDTO;

import java.util.List;

public interface SuggestedProductService {

    Page<SuggestedProductDTO> getAll(Pageable pageable);

    ResponseEntity<?> save(Integer productId);

    ResponseEntity<?> delete(List<Integer> suggestedProductIds);
}
