package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import usoft.cdm.electronics_market.model.ProductForHomePage;
import usoft.cdm.electronics_market.model.ProductsDTO;

import java.util.List;

public interface ProductRepositoryCustom {

    List<ProductsDTO> getAllMadeByProducts(Integer categoryId);

    List<ProductsDTO> getRelatedProducts(Integer categoryId);

    List<ProductForHomePage> getProductsForHomePage(Integer categoryId);

    List<ProductsDTO> getProductsInSameBrand(Integer brandId);

    List<ProductsDTO> getAllMadeByProducts(List<Integer> categoryIds);

    Page<ProductsDTO> findByBrandAndPriceAndMadeIn(List<Integer> categoryIds, ProductsDTO dto, Pageable pageable);

    Page<ProductsDTO> findByBrandAndPriceAndMadeIn(Integer categoryId, ProductsDTO dto, Pageable pageable);

    Page<ProductsDTO> searchNameForHomepage(String name, Pageable pageable);

    ProductsDTO getDiscountByCategory(Integer categoryId);
}
