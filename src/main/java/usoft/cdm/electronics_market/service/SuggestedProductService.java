package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SuggestedProductService {

    ResponseEntity<?> save(Integer productId);

    ResponseEntity<?> delete(List<Integer> suggestedProductIds);
}
