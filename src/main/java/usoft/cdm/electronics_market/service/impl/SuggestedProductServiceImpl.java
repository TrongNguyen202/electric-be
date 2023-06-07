package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.*;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.model.SuggestedProductDTO;
import usoft.cdm.electronics_market.repository.*;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.service.SuggestedProductService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class SuggestedProductServiceImpl implements SuggestedProductService {

    private final SuggestedProductRepository suggestedProductRepository;

    private final ProductRepository productRepository;
    private final UserService userService;

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    private final ProductService productService;


    @Override
    public Page<SuggestedProductDTO> getAll(Pageable pageable) {
        Page<SuggestedProduct> suggestedProducts = this.suggestedProductRepository.findAll(pageable);
        Page<SuggestedProductDTO> suggestedProductDTOS = MapperUtil.mapEntityPageIntoDtoPage(suggestedProducts, SuggestedProductDTO.class);
        for (SuggestedProductDTO dto : suggestedProductDTOS) {
            Products products = this.productRepository.findById(dto.getProductId()).orElseThrow();
            Category category = this.categoryRepository.findById(products.getCategoryId()).orElseThrow();
            Brand brand = this.brandRepository.findById(products.getBrandId()).orElseThrow();
            List<String> imgs = this.productService.getImgs(dto.getProductId(), 2);
            dto.setBrandName(brand.getName());
            dto.setNameCategory(category.getName());
            dto.setNameProduct(products.getName());
            dto.setPriceAfterSale(products.getPriceAfterSale());
            dto.setPriceSell(products.getPriceSell());
            dto.setImg(imgs);
            if (null == dto.getPriceAfterSale()) {
                dto.setDiscount(0.0);
            } else {
                Double discountPercent = (dto.getPriceAfterSale() / dto.getPriceSell()) * 100;
                Double discount = 100 - discountPercent;
                dto.setDiscount(discount);
            }

        }
        return suggestedProductDTOS;
    }

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
        List<SuggestedProduct> suggestedProducts = new ArrayList<>();
        suggestedProductIds.forEach(suggestedProductId -> {
            Optional<SuggestedProduct> optional = this.suggestedProductRepository.findById(suggestedProductId);
            if (optional.isEmpty()) {
                throw new BadRequestException("Không tìm thấy id của kho");
            }
            SuggestedProduct hotCategory = optional.get();
            suggestedProducts.add(hotCategory);
        });
        this.suggestedProductRepository.deleteAll(suggestedProducts);
        return ResponseUtil.message(Message.REMOVE);
    }
}
