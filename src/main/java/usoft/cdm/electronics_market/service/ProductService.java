package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.model.TitleAttributeDTO;

import java.util.List;

public interface ProductService {

    ResponseEntity<?> getAllProducts(Pageable pageable);

    ResponseEntity<?> getByProductId(Integer productId);

    ResponseEntity<?> save(ProductsDTO dto, List<String> imgList, List<TitleAttributeDTO> titleAttributeDTOs);

    ResponseEntity<?> update(ProductsDTO dto, List<String> imgList, List<TitleAttributeDTO> titleAttributeDTOs);

    ResponseEntity<?> deleteProductByIds(List<Integer> idProducts);

    ResponseEntity<?> getAllProductAndCategoryForHome();

    Page<ProductsDTO> getAllProductFromCategoryId(Integer categoryId, Pageable pageable, ProductsDTO dto);

    ResponseEntity<?> getRelatedProducts(Integer productId);

    ResponseEntity<?> getProductsInSameBrand(Integer productId);

    Page<Products> searchByCategory(Integer categoryId, Pageable pageable);

    Page<Products> searchByName(String name, Pageable pageable);

    List<String> getImgs(Integer id, Integer type);

    void setDiscount(ProductsDTO dto);

    ResponseEntity<?> searchNameForHomepage(String name, Pageable pageable);

}
