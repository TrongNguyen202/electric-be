package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.BrandDTO;
import usoft.cdm.electronics_market.model.ImageDTO;

import java.util.List;

public interface BrandService {
    ResponseEntity<?> getAll(String type);

    ResponseEntity<?> getAllList();

    ResponseEntity<?> getById(Integer id);

    ResponseEntity<?> search(String name, Pageable pageable);

    ResponseEntity<?> searchForHotBrand(String name);

    ResponseEntity<?> getPage(Pageable pageable);

    ResponseEntity<?> save(BrandDTO dto, List<String> images);

    ResponseEntity<?> update(BrandDTO dto, List<ImageDTO> images);

    ResponseEntity<?> remove(List<Integer> ids);
}
