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

    Page<ProductsDTO> getProductsInSameBrand(Integer brandId, Pageable pageable);

    List<ProductsDTO> getAllMadeByProducts(List<Integer> categoryIds);

    Page<ProductsDTO> findByBrandAndPriceAndMadeIn(List<Integer> categoryIds, ProductsDTO dto, Pageable pageable);

    Page<ProductsDTO> findByPriceAndCategory(Integer brandId, ProductsDTO dto, Pageable pageable);

    Page<ProductsDTO> findByPriceAndCategoryList(Integer brandId, List<Integer> categoryIds, ProductsDTO dto, Pageable pageable);

    Page<ProductsDTO> findByBrandAndPriceAndMadeIn(Integer categoryId, ProductsDTO dto, Pageable pageable);

    Page<ProductsDTO> findByBrandAndPriceAndMadeInForSearchProduct(String name, ProductsDTO dto, Pageable pageable);

    Page<ProductsDTO> searchNameForHomepage(String name, Pageable pageable);

    ProductsDTO getDiscountByCategory(Integer categoryId);

    List<ProductsDTO> getMadeInForNameProduct(String name);

    ProductsDTO getRangePriceForNameProduct(String name, Double priceFrom, Double priceTo);
}
