package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import usoft.cdm.electronics_market.model.BrandDTO;

public interface BrandRepositoryCustom {

    Page<BrandDTO> getAllBrandByCategoryId(Integer categoryId, Pageable pageable);
}
