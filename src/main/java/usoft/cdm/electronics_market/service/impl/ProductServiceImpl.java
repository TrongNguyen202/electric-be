package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.TextUtil;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final UserService userService;

    @Override
    public ResponseEntity<?> save(ProductsDTO dto, List<String> imgList) {
        Users userLogin = this.userService.getCurrentUser();
        Products products = Products
                .builder()
                .code(dto.getCode())
                .name(dto.getName())
                .slug(TextUtil.slug(dto.getName()))
                .warehouseId(dto.getWarehouseId())
                .brandId(dto.getBrandId())
                .categoryId(dto.getCategoryId())
                .priceImport(dto.getPriceImport())
                .priceSell(dto.getPriceSell())
                .capicityId(dto.getCapicityId())
                .priceAfterSale(dto.getPriceAfterSale())
                .build();

        return null;
    }
}
