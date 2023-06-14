package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.entities.ProductPromotion;
import usoft.cdm.electronics_market.model.ProductPromotionDTO;
import usoft.cdm.electronics_market.model.PromotionDTO;

import java.util.List;

public interface PromotionService {

    Page<PromotionDTO> findAll(Pageable pageable);

    ResponseEntity<?> save(PromotionDTO dto, List<ProductPromotionDTO> productPromotionDTOS);

    ResponseEntity<?> update(PromotionDTO dto, List<ProductPromotionDTO> productPromotionDTOS);

    ResponseEntity<?> getById(Integer promotionId);

    ResponseEntity<?> delete(List<Integer> promotionIds);
}
