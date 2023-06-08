package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.entities.FlashSale;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.FlashSaleDTO;
import usoft.cdm.electronics_market.repository.FlashSaleRepository;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.service.FlashSaleService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class FlashSaleServiceImpl implements FlashSaleService {

    private final FlashSaleRepository flashSaleRepository;

    private final UserService userService;

    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<?> save(FlashSaleDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Products> products = this.productRepository.findById(dto.getProductId());
        if (products.isEmpty()) {
            throw new BadRequestException("Không có sản phẩm này");
        }
        if (dto.getQuantitySale() > products.get().getQuantity()) {
            throw new BadRequestException("Số lượng sale vượt quá số lượng trong kho");
        }

        if (dto.getQuantitySale() == 0) {
            throw new BadRequestException("Phải nhập số lượng sale ");
        }
        FlashSale flashSale = FlashSale
                .builder()
                .priceFlashSale(dto.getPriceFlashSale())
                .startSale(dto.getStartSale())
                .endSale(dto.getEndSale())
                .description(dto.getDescription())
                .productId(dto.getProductId())
                .quantitySale(dto.getQuantitySale())
                .build();
        flashSale.setCreatedBy(userLogin.getUsername());
        this.flashSaleRepository.save(flashSale);
        return ResponseUtil.ok(flashSale);
    }
}
