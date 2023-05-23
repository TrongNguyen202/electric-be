package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.CategoryDTO;

import java.util.List;

public interface CategoryService {

    ResponseEntity<?> save(CategoryDTO dto, List<String> imgList);

    ResponseEntity<?> displayById(Integer idCategory);

    Page<CategoryDTO> findByAll(Pageable pageable);

    ResponseEntity<?> update(CategoryDTO dto, List<String> imgList);

    ResponseEntity<?> saveChildCategory(CategoryDTO dto);

    ResponseEntity<?> updateChildCategory(CategoryDTO dto);
}
