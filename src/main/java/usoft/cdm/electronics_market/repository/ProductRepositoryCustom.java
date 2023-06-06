package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import usoft.cdm.electronics_market.model.ProductsDTO;

public interface ProductRepositoryCustom {

    Page<ProductsDTO> getAllMadeByProducts(Integer categoryId, Pageable pageable);

    Page<ProductsDTO> findByBrandAndPriceAndMadeIn(Integer categoryId, ProductsDTO dto, Pageable pageable);

    Page<ProductsDTO> searchNameForHomepage(String name, Pageable pageable);
}
