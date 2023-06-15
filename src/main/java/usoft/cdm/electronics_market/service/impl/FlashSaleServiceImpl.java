package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.Brand;
import usoft.cdm.electronics_market.entities.FlashSale;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.FlashSaleDTO;
import usoft.cdm.electronics_market.repository.BrandRepository;
import usoft.cdm.electronics_market.repository.FlashSaleRepository;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.service.FlashSaleService;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class FlashSaleServiceImpl implements FlashSaleService {

    private final FlashSaleRepository flashSaleRepository;

    private final UserService userService;

    private final ProductRepository productRepository;

    private final BrandRepository brandRepository;

    private final ProductService productService;

    @Override
    public ResponseEntity<?> save(FlashSaleDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Products> products = this.productRepository.findByIdAndStatus(dto.getProductId(), true);
        if (products.isEmpty()) {
            throw new BadRequestException("Không có sản phẩm này");
        }

        FlashSale saleList = this.flashSaleRepository.findByProductIdAndStatus(products.get().getId(), true);

        if (saleList != null) {
            throw new BadRequestException("Sản phẩm này đã có flashsale rồi");
        }
        if (dto.getQuantitySale() > products.get().getQuantity()) {
            throw new BadRequestException("Số lượng vượt quá quy định ");
        }

        if (dto.getQuantitySale() == 0) {
            throw new BadRequestException("Phải nhập số lượng sale ");
        }
        LocalDateTime currentDateTime = LocalDateTime.now().minusMinutes(2);
        if (dto.getEndSaleInput().isBefore(currentDateTime)) {
            throw new BadRequestException("Ngày hết hạn sớm quá ");
        }
        if (dto.getStartSaleInput().isBefore(currentDateTime)) {
            throw new BadRequestException("Ngày bắt đầu trôi qua rồi");
        }
        ZoneId zoneId = ZoneId.of("GMT+7");
        ZonedDateTime dateTimeStartSale = ZonedDateTime.of(dto.getStartSaleInput(), zoneId);
        ZonedDateTime dateTimeEndSale = ZonedDateTime.of(dto.getEndSaleInput(), zoneId);
        FlashSale flashSale = FlashSale
                .builder()
                .priceFlashSale(dto.getPriceFlashSale())
                .startSale(dateTimeStartSale)
                .endSale(dateTimeEndSale)
                .description(dto.getDescription())
                .productId(dto.getProductId())
                .quantitySale(dto.getQuantitySale())
                .status(true)
                .build();
        flashSale.setCreatedBy(userLogin.getUsername());
        this.flashSaleRepository.save(flashSale);
        return ResponseUtil.ok(flashSale);
    }

    @Override
    public ResponseEntity<?> update(FlashSaleDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<FlashSale> flashSaleOptional = this.flashSaleRepository.findByIdAndStatus(dto.getId(), true);
        if (flashSaleOptional.isEmpty()) {
            throw new BadRequestException("Không có id của flashsale này");
        }
        Optional<Products> products = this.productRepository.findByIdAndStatus(dto.getProductId(), true);
        if (products.isEmpty()) {
            throw new BadRequestException("Không có sản phẩm này");
        }
        FlashSale saleList = this.flashSaleRepository.findByProductIdAndStatus(products.get().getId(), true);
        if (saleList == null) {
            FlashSale saleListCheck = this.flashSaleRepository.findByProductIdAndStatus(dto.getProductId(), true);
            if (saleListCheck != null) {
                throw new BadRequestException("Sản phẩm này đã có flashsale rồi");
            }
        }

        if (dto.getQuantitySale() > products.get().getQuantity()) {
            throw new BadRequestException("Số lượng vượt quá quy định ");
        }

        if (dto.getQuantitySale() == 0) {
            throw new BadRequestException("Phải nhập số lượng sale ");
        }
        LocalDateTime currentDateTime = LocalDateTime.now().minusMinutes(2);
        if (dto.getEndSaleInput().isBefore(currentDateTime)) {
            throw new BadRequestException("Ngày hết hạn sớm quá ");
        }
        if (dto.getStartSaleInput().isBefore(currentDateTime)) {
            throw new BadRequestException("Ngày bắt đầu trôi qua rồi");
        }
        ZoneId zoneId = ZoneId.of("GMT+7");
        ZonedDateTime dateTimeStartSale = ZonedDateTime.of(dto.getStartSaleInput(), zoneId);
        ZonedDateTime dateTimeEndSale = ZonedDateTime.of(dto.getEndSaleInput(), zoneId);
        FlashSale flashSale = MapperUtil.map(dto, FlashSale.class);
        flashSale.setStatus(true);
        flashSale.setStartSale(dateTimeStartSale);
        flashSale.setEndSale(dateTimeEndSale);
        flashSale.setUpdatedBy(userLogin.getUpdatedBy());
        this.flashSaleRepository.save(flashSale);
        return ResponseUtil.ok(flashSale);
    }

    @Override
    public ResponseEntity<?> getById(Integer flashSaleId) {
        Optional<FlashSale> flashSaleOptional = this.flashSaleRepository.findByIdAndStatus(flashSaleId, true);
        if (flashSaleOptional.isEmpty()) {
            throw new BadRequestException("Không có id của flashsale này");
        }
        return ResponseUtil.ok(flashSaleOptional.get());
    }

    @Override
    public Page<FlashSaleDTO> getAll(Pageable pageable) {
        Page<FlashSale> flashSales = this.flashSaleRepository.findByStatus(pageable, true);
        Page<FlashSaleDTO> flashSaleDTOS = MapperUtil.mapEntityPageIntoDtoPage(flashSales, FlashSaleDTO.class);
        for (FlashSaleDTO dto : flashSaleDTOS) {
            Optional<Products> productsOptional = this.productRepository.findByIdAndStatus(dto.getProductId(), true);
            if (productsOptional.isEmpty()) {
                throw new BadRequestException("Không có sản phẩm này");
            }
            Products products = productsOptional.get();
            dto.setNameProduct(products.getName());
            dto.setPriceSell(products.getPriceSell());
            dto.setSlug(products.getSlug());
            Optional<Brand> brand = this.brandRepository.findByIdAndStatus(products.getBrandId(), true);
            dto.setBrandName(brand.get().getName());
            List<String> imgs = this.productService.getImgs(products.getId(), 2);
            dto.setImgs(imgs);
            if (null == dto.getPriceSell()) {
                dto.setDiscount(0.0);
            } else {
                Double discountPercent = (dto.getPriceFlashSale() / dto.getPriceSell()) * 100;
                Double discount = 100 - discountPercent;
                dto.setDiscount(discount);

            }

        }
        return flashSaleDTOS;
    }

    @Override
    public ResponseEntity<?> delete(List<Integer> flashSaleIds) {
        Users userLogin = this.userService.getCurrentUser();
        List<FlashSale> flashSaleList = new ArrayList<>();
        flashSaleIds.forEach(flashSaleId -> {
            Optional<FlashSale> flashSaleOptional = this.flashSaleRepository.findByIdAndStatus(flashSaleId, true);
            if (flashSaleOptional.isEmpty()) {
                throw new BadRequestException("Không tìm thấy id của kho");
            }
            FlashSale warehouse = flashSaleOptional.get();
            warehouse.setUpdatedBy(userLogin.getUsername());
            warehouse.setStatus(false);
            flashSaleList.add(warehouse);
        });
        this.flashSaleRepository.saveAll(flashSaleList);
        return ResponseUtil.message(Message.REMOVE);
    }

    @Scheduled(cron = "0 0 0 * * ?")//s m h d m y
    public void UpdateStatusAfterEndDate() {
        List<FlashSale> flashSales = this.flashSaleRepository.findByStatus(true);
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<FlashSale> saleList = new ArrayList<>();
        for (FlashSale flashSale : flashSales) {
            if (flashSale.getEndSale().toLocalDateTime().isBefore(currentDateTime)) {
                flashSale.setStatus(false);
                saleList.add(flashSale);
            }
        }
        this.flashSaleRepository.saveAll(saleList);
    }
}
