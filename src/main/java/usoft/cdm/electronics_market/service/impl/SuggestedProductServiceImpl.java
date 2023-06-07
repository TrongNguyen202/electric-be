package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.entities.SuggestedProduct;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.repository.SuggestedProductRepository;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.service.SuggestedProductService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class SuggestedProductServiceImpl implements SuggestedProductService {

    private final SuggestedProductRepository suggestedProductRepository;

    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    public ResponseEntity<?> save(Integer productId) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Products> products = this.productRepository.findById(productId);
        if (products.isEmpty()) {
            throw new BadRequestException("Không tìm thấy sản phẩm này");
        }
        SuggestedProduct suggestedProduct = SuggestedProduct
                .builder()
                .productId(products.get().getId())
                .build();
        suggestedProduct.setCreatedBy(userLogin.getUsername());
        this.suggestedProductRepository.save(suggestedProduct);
        return ResponseUtil.ok(suggestedProduct);
    }

    @Override
    public ResponseEntity<?> delete(List<Integer> suggestedProductIds) {
        return null;
    }
}
