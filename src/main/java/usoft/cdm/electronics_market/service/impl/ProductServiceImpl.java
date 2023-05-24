package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.entities.*;
import usoft.cdm.electronics_market.model.AttributeDTO;
import usoft.cdm.electronics_market.model.ProductDetailDTO;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.model.TitleAttributeDTO;
import usoft.cdm.electronics_market.repository.AttributeRepository;
import usoft.cdm.electronics_market.repository.ImageRepository;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.repository.TitleAttibuteRepository;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;
import usoft.cdm.electronics_market.util.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final UserService userService;

    private final ImageRepository imageRepository;

    private final TitleAttibuteRepository titleAttibuteRepository;

    private final AttributeRepository attributeRepository;


    @Override
    public ResponseEntity<?> getAllProducts(Pageable pageable) {
        Page<Products> productsPage = this.productRepository.findAllByStatus(true, pageable);
        Page<ProductsDTO> productsDTOS = MapperUtil.mapEntityPageIntoDtoPage(productsPage, ProductsDTO.class);
        return ResponseUtil.ok(productsDTOS);
    }

    @Override
    public ResponseEntity<?> getByProductId(Integer productId) {
        Optional<Products> optionalProducts = this.productRepository.findById(productId);
        if (optionalProducts.isEmpty()) {
            throw new BadRequestException("Không có id sản phẩm");
        } else {
            List<Image> image = this.imageRepository.findByDetailIdAndType(productId, 2);
            List<String> imgs = image.stream().map(Image::getImg).collect(Collectors.toList());
            ProductsDTO productsDTO = MapperUtil.map(optionalProducts.get(), ProductsDTO.class);
            productsDTO.setImg(imgs);
            List<TitleAttribute> titleAttributes = this.titleAttibuteRepository.findByProductId(productId);
            List<TitleAttributeDTO> titleAttributeDTOS = MapperUtil.mapList(titleAttributes, TitleAttributeDTO.class);
            for (TitleAttributeDTO titleAttributeDTO : titleAttributeDTOS) {
                List<Attribute> attributes = this.attributeRepository.findByTitleAttributeId(titleAttributeDTO.getId());
                List<AttributeDTO> attributeDTOS = MapperUtil.mapList(attributes, AttributeDTO.class);
                titleAttributeDTO.setAttributeDTOS(attributeDTOS);
            }
            productsDTO.setDto(titleAttributeDTOS);
            return ResponseUtil.ok(productsDTO);
        }
    }

    @Override
    public ResponseEntity<?> save(ProductsDTO
                                          dto, List<String> imgList, List<TitleAttributeDTO> titleAttributeDTOs) {
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
                .information(dto.getInformation())
                .quantity(dto.getQuantity())
                .status(true)
                .build();
        products.setCreatedBy(userLogin.getUsername());
        Products productsSave = this.productRepository.save(products);
        List<Image> images = new ArrayList<>();
        for (String img : imgList) {
            Image image = Image
                    .builder()
                    .img(img)
                    .detailId(productsSave.getId())
                    .type(2)
                    .build();
            images.add(image);
        }
        this.imageRepository.saveAll(images);

        for (TitleAttributeDTO titleAttributeDTO : titleAttributeDTOs) {
            TitleAttribute titleAttribute = TitleAttribute
                    .builder()
                    .name(titleAttributeDTO.getName())
                    .productId(productsSave.getId())
                    .build();
            TitleAttribute titleAttributeNew = this.titleAttibuteRepository.save(titleAttribute);
            titleAttributeDTO.getAttributeDTOS().forEach(attributeDTO -> {
                Attribute attribute = Attribute
                        .builder()
                        .titleAttributeId(titleAttributeNew.getId())
                        .name(attributeDTO.getName())
                        .value(attributeDTO.getValue())
                        .build();
                this.attributeRepository.save(attribute);
            });
        }

        return ResponseUtil.ok(products);
    }

    @Override
    public ResponseEntity<?> update(ProductsDTO
                                            dto, List<String> imgList, List<TitleAttributeDTO> titleAttributeDTOs) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Products> optionalProducts = this.productRepository.findById(dto.getId());
        if (optionalProducts.isEmpty()) {
            throw new BadRequestException("Không có id sản phẩm");
        } else {
            Products products = MapperUtil.map(dto, Products.class);
            products.setUpdatedBy(userLogin.getUsername());
            products.setSlug(TextUtil.slug(dto.getName()));
            products.setStatus(true);
            this.productRepository.save(products);
            List<Image> imageList = this.imageRepository.findByDetailIdAndType(dto.getId(), 2);
            this.imageRepository.deleteAll(imageList);
            List<Image> images = new ArrayList<>();
            for (String img : imgList) {
                Image image = Image
                        .builder()
                        .img(img)
                        .detailId(dto.getId())
                        .type(2)
                        .build();
                images.add(image);
            }
            this.imageRepository.saveAll(images);
            for (TitleAttributeDTO titleAttributeDTO : titleAttributeDTOs) {
                Optional<TitleAttribute> optionalTitleAttribute = this.titleAttibuteRepository.findById(titleAttributeDTO.getId());
                if (optionalTitleAttribute.isEmpty()) {
                    throw new BadRequestException("Không tìm thấy id tiêu đề thuộc tính");
                } else {
                    TitleAttribute titleAttribute = MapperUtil.map(titleAttributeDTO, TitleAttribute.class);
                    titleAttribute.setProductId(products.getId());
                    this.titleAttibuteRepository.save(titleAttribute);
                    titleAttributeDTO.getAttributeDTOS().forEach(attributeDTO -> {
                        Optional<Attribute> optionalAttribute = this.attributeRepository.findById(attributeDTO.getId());
                        if (optionalAttribute.isEmpty()) {
                            throw new BadRequestException("Không tìm thấy id thuộc tính");
                        } else {
                            Attribute attribute = MapperUtil.map(attributeDTO, Attribute.class);
                            attribute.setTitleAttributeId(titleAttribute.getId());
                            this.attributeRepository.save(attribute);
                        }
                    });
                }
            }


        }
        return ResponseUtil.ok(optionalProducts.get());
    }


}
