package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.HotBrandDTO;

import java.util.List;

public interface HotBrandService {

    ResponseEntity<?> save(HotBrandDTO dto);

    Page<HotBrandDTO> findAll(Pageable pageable);

    ResponseEntity<?> delete(List<Integer> hotBrandIds);
}
