package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.entities.Image;
import usoft.cdm.electronics_market.model.CategoryDTO;

import java.util.List;

public interface CategoryService {

    ResponseEntity<?> save(CategoryDTO dto, List<String> imgList);

    ResponseEntity<?> update(CategoryDTO dto);
}
