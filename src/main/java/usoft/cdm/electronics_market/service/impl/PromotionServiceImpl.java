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
import usoft.cdm.electronics_market.entities.*;
import usoft.cdm.electronics_market.model.ProductPromotionDTO;
import usoft.cdm.electronics_market.model.PromotionDTO;
import usoft.cdm.electronics_market.repository.*;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.service.PromotionService;
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
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    private final ProductPromotionRepository productPromotionRepository;

    private final ProductRepository productRepository;


    private final BrandRepository brandRepository;

    private final ProductService productService;

    private final UserService userService;

    @Override
    public Page<PromotionDTO> findAll(Pageable pageable) {
        Page<Promotion> promotionPage = this.promotionRepository.findAllByStatus(true, pageable);
        Page<PromotionDTO> dtos = MapperUtil.mapEntityPageIntoDtoPage(promotionPage, PromotionDTO.class);
        for (PromotionDTO dto : dtos) {
            List<ProductPromotion> productPromotions = this.productPromotionRepository.findAllByPromotionId(dto.getId());
            List<ProductPromotionDTO> promotionDTOList = MapperUtil.mapList(productPromotions, ProductPromotionDTO.class);
            dto.setProductPromotionDTOList(promotionDTOList);
        }
        return dtos;
    }

    @Override
    public ResponseEntity<?> save(PromotionDTO dto, List<ProductPromotionDTO> productPromotionDTOS) {
        Users userLogin = this.userService.getCurrentUser();
        LocalDateTime currentDateTime = LocalDateTime.now().minusMinutes(2);
        if (dto.getEndSaleInput().isBefore(currentDateTime)) {
            throw new BadRequestException("Ngày hết hạn sớm quá ");
        }
        ZoneId zoneId = ZoneId.of("GMT+7");
        ZonedDateTime dateTimeEndSale = ZonedDateTime.of(dto.getEndSaleInput(), zoneId);
        Promotion promotion = Promotion
                .builder()
                .bannerImg(dto.getBannerImg())
                .status(true)
                .description(dto.getDescription())
                .content(dto.getContent())
                .name(dto.getName())
                .endSale(dateTimeEndSale)
                .build();
        promotion.setCreatedBy(userLogin.getUsername());
        Promotion promotionSave = this.promotionRepository.save(promotion);
        List<ProductPromotion> productPromotionList = new ArrayList<>();
        for (ProductPromotionDTO productPromotionDTO : productPromotionDTOS) {
            if (productPromotionDTO.getProductId() == null) {
                throw new BadRequestException("Không tìm thấy sản phẩm này");
            }
            Optional<Products> productCheck = this.productRepository.findByIdAndStatus(productPromotionDTO.getProductId(), true);
            if (productCheck.isEmpty()) {
                throw new BadRequestException("Không tìm thấy sản phẩm này");
            }
            Products products = productCheck.get();
            List<String> imgs = this.productService.getImgs(products.getId(), 2);
            Optional<Brand> brandOptional = this.brandRepository.findByIdAndStatus(products.getBrandId(), true);
            ProductPromotion productPromotion = ProductPromotion
                    .builder()
                    .promotionId(promotionSave.getId())
                    .productId(productPromotionDTO.getProductId())
                    .priceSell(products.getPriceSell())
                    .brandName(brandOptional.get().getName())
                    .description(productPromotionDTO.getDescription())
                    .priceAfterSale(products.getPriceAfterSale())
                    .name(products.getName())
                    .build();
            if (imgs.isEmpty()) {
                productPromotion.setImg(null);
            } else {
                productPromotion.setImg(imgs.get(0));
            }
            productPromotionList.add(productPromotion);
        }
        this.productPromotionRepository.saveAll(productPromotionList);

        return ResponseUtil.ok(promotionSave);
    }

    @Override
    public ResponseEntity<?> update(PromotionDTO dto, List<ProductPromotionDTO> productPromotionDTOS) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Promotion> optionalPromotion = this.promotionRepository.findAllByIdAndStatus(dto.getId(), true);
        if (optionalPromotion.isEmpty()) {
            throw new BadRequestException("Không có id của khuyến mãi này");
        }
        LocalDateTime currentDateTime = LocalDateTime.now().minusMinutes(2);
        if (dto.getEndSaleInput().isBefore(currentDateTime)) {
            throw new BadRequestException("Ngày hết hạn sớm quá ");
        }
        ZoneId zoneId = ZoneId.of("GMT+7");
        ZonedDateTime dateTimeEndSale = ZonedDateTime.of(dto.getEndSaleInput(), zoneId);
        Promotion promotion = MapperUtil.map(dto, Promotion.class);
        promotion.setStatus(true);
        promotion.setEndSale(dateTimeEndSale);
        promotion.setUpdatedBy(userLogin.getUsername());
        this.promotionRepository.save(promotion);
        List<ProductPromotion> productPromotionNews = new ArrayList<>();
        List<Integer> productPromotionsOLd = this.productPromotionRepository.findIdAllByPromotionId(dto.getId());
        for (ProductPromotionDTO productPromotionDTO : productPromotionDTOS) {
            if (productPromotionDTO.getProductId() == null) {
                throw new BadRequestException("Không tìm thấy sản phẩm này");
            }
            Optional<Products> productCheck = this.productRepository.findByIdAndStatus(productPromotionDTO.getProductId(), true);
            if (productCheck.isEmpty()) {
                throw new BadRequestException("Không tìm thấy sản phẩm này");
            }
            Products products = productCheck.get();
            List<String> imgs = this.productService.getImgs(products.getId(), 2);
            Optional<Brand> brandOptional = this.brandRepository.findByIdAndStatus(products.getBrandId(), true);
            if (productPromotionDTO.getId() != null) {
                productPromotionsOLd.remove(productPromotionDTO.getId());
                ProductPromotion promotionNew = this.productPromotionRepository.findById(productPromotionDTO.getId()).orElse(new ProductPromotion());
                promotionNew.setProductId(productPromotionDTO.getProductId());
                promotionNew.setPromotionId(dto.getId());
                promotionNew.setBrandName(brandOptional.get().getName());
                promotionNew.setDescription(productPromotionDTO.getDescription());
                promotionNew.setName(products.getName());
                promotionNew.setPriceSell(products.getPriceSell());
                promotionNew.setPriceAfterSale(products.getPriceAfterSale());
                if (imgs.isEmpty()) {
                    promotionNew.setImg(null);
                } else {
                    promotionNew.setImg(imgs.get(0));
                }
                productPromotionNews.add(promotionNew);
            } else {
                ProductPromotion productPromotionNew = ProductPromotion
                        .builder()
                        .promotionId(dto.getId())
                        .productId(productPromotionDTO.getProductId())
                        .priceSell(products.getPriceSell())
                        .brandName(brandOptional.get().getName())
                        .description(productPromotionDTO.getDescription())
                        .priceAfterSale(products.getPriceAfterSale())
                        .name(products.getName())
                        .build();
                if (imgs.isEmpty()) {
                    productPromotionNew.setImg(null);
                } else {
                    productPromotionNew.setImg(imgs.get(0));
                }
                productPromotionNews.add(productPromotionNew);
            }
        }
        this.productPromotionRepository.saveAll(productPromotionNews);
        productPromotionRepository.deleteAllById(productPromotionsOLd);
        return ResponseUtil.ok(productPromotionNews);
    }

    @Override
    public ResponseEntity<?> getById(Integer promotionId) {
        Optional<Promotion> optionalPromotion = this.promotionRepository.findAllByIdAndStatus(promotionId, true);
        if (optionalPromotion.isEmpty()) {
            throw new BadRequestException("Không có id của khuyến mãi này");
        }
        Promotion promotion = optionalPromotion.get();
        PromotionDTO dto = MapperUtil.map(promotion, PromotionDTO.class);
        List<ProductPromotion> productPromotions = this.productPromotionRepository.findAllByPromotionId(promotionId);
        List<ProductPromotionDTO> productPromotionDTO = MapperUtil.mapList(productPromotions, ProductPromotionDTO.class);
        dto.setProductPromotionDTOList(productPromotionDTO);
        return ResponseUtil.ok(dto);
    }

    @Override
    public ResponseEntity<?> delete(List<Integer> promotionIds) {
        Users userLogin = this.userService.getCurrentUser();
        List<Promotion> promotionList = new ArrayList<>();
        promotionIds.forEach(promotionId -> {
            Optional<Promotion> promotionOptional = this.promotionRepository.findAllByIdAndStatus(promotionId, true);
            if (promotionOptional.isEmpty()) {
                throw new BadRequestException("Không có id của khuyến mãi này");
            }
            Promotion promotion = promotionOptional.get();
            promotion.setUpdatedBy(userLogin.getUsername());
            promotion.setStatus(false);
            promotionList.add(promotion);
        });
        this.promotionRepository.saveAll(promotionList);
        return ResponseUtil.message(Message.REMOVE);
    }

    @Scheduled(cron = "0 0 0 * * ?")//s m h d m y
    public void UpdateStatusAfterEndDate() {
        List<Promotion> promotionList = this.promotionRepository.findAllByStatus(true);
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Promotion> promotions = new ArrayList<>();
        for (Promotion promotion : promotionList) {
            if (promotion.getEndSale().toLocalDateTime().isBefore(currentDateTime)) {
                promotion.setStatus(false);
                promotions.add(promotion);
            }
        }
        this.promotionRepository.saveAll(promotionList);
    }
}
