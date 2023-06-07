package usoft.cdm.electronics_market.repository;

import usoft.cdm.electronics_market.model.BrandDTO;

import java.util.List;

public interface BrandRepositoryCustom {

    List<BrandDTO> getAllBrandByCategoryId(Integer categoryId);
}
