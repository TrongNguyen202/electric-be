package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    ResponseEntity<?> getAllProductFromCategoryId(Integer categoryId);

    ResponseEntity<?> getRelatedProducts(Integer productId);

    ResponseEntity<?> getProductsInSameBrand(Integer productId);
}
