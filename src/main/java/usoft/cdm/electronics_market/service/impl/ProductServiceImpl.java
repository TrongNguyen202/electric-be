package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.service.ProductService;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<?> save(ProductsDTO dto) {
        Products products = Products
                .builder()
                .code(dto.getCode())
                .warehouseId(dto.getWarehouseId())
                .brandId(dto.getBrandId())
                .categoryId(dto.getCategoryId())
                .priceImport(dto.getPriceImport())
                .priceSell(dto.getPriceSell())
                .capicityId(dto.getCapicityId())
                .discount(dto.getDiscount())
                .build();

        return null;
    }
}
