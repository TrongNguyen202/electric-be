package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.HotCategoryDTO;

import java.util.List;

public interface HotCategoryService {

    Page<HotCategoryDTO> findAll(Pageable pageable);

    ResponseEntity<?> save(HotCategoryDTO dto);

    ResponseEntity<?> update(HotCategoryDTO dto);

    ResponseEntity<?> getById(Integer hotCategoryId);

    ResponseEntity<?> delete(List<Integer> hotCategoryIds);
}
