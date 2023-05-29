package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.*;
import usoft.cdm.electronics_market.model.*;
import usoft.cdm.electronics_market.repository.*;
import usoft.cdm.electronics_market.service.CategoryService;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;
import usoft.cdm.electronics_market.util.TextUtil;

import java.util.*;
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

    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    private final WarehouseRepository warehouseRepository;


    @Override
    public ResponseEntity<?> getAllProducts(Pageable pageable) {
        Page<Products> productsPage = this.productRepository.findAllByStatus(true, pageable);
        Page<ProductsDTO> productsDTOS = MapperUtil.mapEntityPageIntoDtoPage(productsPage, ProductsDTO.class);
        productsDTOS.forEach(productsDTO -> {
            Brand brand = this.brandRepository.findById(productsDTO.getBrandId()).orElseThrow();
            productsDTO.setBrandName(brand.getName());
            Category category = this.categoryRepository.findById(productsDTO.getCategoryId()).orElseThrow();
            productsDTO.setCategoryName(category.getName());
            Warehouse warehouse = this.warehouseRepository.findById(productsDTO.getWarehouseId()).orElseThrow();
            productsDTO.setWarehouseName(warehouse.getName());
        });

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
            Category category = this.categoryRepository.findById(productsDTO.getCategoryId()).orElseThrow();
            productsDTO.setCategoryName(category.getName());
            Brand brand = this.brandRepository.findById(productsDTO.getBrandId()).orElseThrow();
            productsDTO.setBrandName(brand.getName());
            Warehouse warehouse = this.warehouseRepository.findById(productsDTO.getWarehouseId()).orElseThrow();
            productsDTO.setWarehouseName(warehouse.getName());
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
    public ResponseEntity<?> save(ProductsDTO dto, List<String> imgList, List<TitleAttributeDTO> titleAttributeDTOs) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Products> productCheck = this.productRepository.findByCodeAndStatus(dto.getCode(), true);
        if (productCheck.isPresent()) {
            throw new BadRequestException("Mã sản phẩm đã tồn tại");
        }

        if (dto.getQuantityImport() <= 0) {
            throw new BadRequestException("Phải nhập số lượng nhập");
        }
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
                .quantity(dto.getQuantityImport())
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
    public ResponseEntity<?> update(ProductsDTO dto, List<String> imgList, List<TitleAttributeDTO> titleAttributeDTOs) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Products> optionalProducts = this.productRepository.findById(dto.getId());

        if (!optionalProducts.get().getCode().equals(dto.getCode())) {
            Optional<Products> productCheck = this.productRepository.findByCodeAndStatus(dto.getCode(), true);
            if (productCheck.isPresent()) {
                throw new BadRequestException("Mã sản phẩm đã tồn tại");
            }
        }
        if (optionalProducts.isEmpty()) {
            throw new BadRequestException("Không có id sản phẩm");
        } else {
            Products products = MapperUtil.map(dto, Products.class);
            products.setQuantity(products.getQuantity() + dto.getQuantityImport());
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

            List<TitleAttribute> titleAttributes = this.titleAttibuteRepository.findByProductId(products.getId());
            List<Integer> titleAttributesIds = titleAttributes.stream().map(TitleAttribute::getId).collect(Collectors.toList());
            List<Attribute> attributes = this.attributeRepository.findByTitleAttributeIdIn(titleAttributesIds);
            this.attributeRepository.deleteAll(attributes);
            this.titleAttibuteRepository.deleteAll(titleAttributes);
            for (TitleAttributeDTO titleAttributeDTO : titleAttributeDTOs) {
                TitleAttribute titleAttribute = TitleAttribute
                        .builder()
                        .name(titleAttributeDTO.getName())
                        .productId(products.getId())
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

        }
        return ResponseUtil.ok(optionalProducts.get());
    }

    @Override
    public ResponseEntity<?> deleteProductByIds(List<Integer> idProducts) {
        List<Products> productsList = new ArrayList<>();
        idProducts.forEach(idProduct -> {
            Optional<Products> optionalProducts = this.productRepository.findById(idProduct);
            if (optionalProducts.isEmpty())
                throw new BadRequestException("Không tìm thấy id sản phẩm");
            Products products = optionalProducts.get();
            products.setStatus(false);
            productsList.add(products);
        });
        this.productRepository.saveAll(productsList);
        return ResponseUtil.message(Message.REMOVE);
    }

    @Override
    public ResponseEntity<?> getAllProductAndCategoryForHome() {
        List<Category> categories = this.categoryRepository.findAllByParentIdIsNullAndStatus(true);
        List<Integer> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
        List<Image> images = this.imageRepository.findByDetailIdInAndType(categoryIds, 1);
        List<String> imgs = images.stream().map(Image::getImg).collect(Collectors.toList());
        Random random = new Random();
        int randomIndex = random.nextInt(imgs.size());
        String imgRandom = imgs.get(randomIndex);
        List<CategoryDTO> categoryDTOS = MapperUtil.mapList(categories, CategoryDTO.class);
        categoryDTOS.forEach(categoryDTO -> {
            List<Category> categoryList = this.categoryRepository.findByParentIdAndStatus(categoryDTO.getId(), true);
            if (categoryList.isEmpty()) {
                List<Products> productsList = this.productRepository.findByStatusAndCategoryId(true, categoryDTO.getId());
                categoryDTO.setProducts(productsList);
            } else {
                List<Integer> categoryIdsNoChild = categoryList.stream().map(Category::getId).collect(Collectors.toList());
                List<Products> products = this.productRepository.findAllByStatusAndCategoryIdIn(true, categoryIdsNoChild);
                categoryDTO.setProducts(products);
            }
            categoryDTO.setCategoryList(categoryList);

            categoryDTO.setPicCategory(imgRandom);

        });

        return ResponseUtil.ok(categoryDTOS);
    }

    @Override
    public Page<ProductsDTO> getAllProductFromCategoryId(Integer categoryId, Pageable pageable) {
        List<Products> products = this.productRepository.findByStatusAndCategoryId(true, categoryId);
        List<Integer> productIds = products.stream().map(Products::getId).collect(Collectors.toList());
        List<Image> imageProduct = this.imageRepository.findByDetailIdInAndType(productIds, 2);
        List<String> imgProduct = imageProduct.stream().map(Image::getImg).collect(Collectors.toList());
        List<ProductsDTO> productsDTOS = MapperUtil.mapList(products, ProductsDTO.class);
        productsDTOS.forEach(productsDTO -> {
            productsDTO.setImg(imgProduct);
            if (null == productsDTO.getPriceAfterSale()) {
                productsDTO.setDiscount(0.0);
            } else {
                Double discountPercent = (productsDTO.getPriceAfterSale() / productsDTO.getPriceSell()) * 100;
                productsDTO.setDiscount(discountPercent);
            }
        });
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > productsDTOS.size() ?
                productsDTOS.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<ProductsDTO> dtosNew = productsDTOS.subList(startIndex, endIndex);
        Page<ProductsDTO> productsDTOPageable = new PageImpl<>(dtosNew, pageable, productsDTOS.size());
        return productsDTOPageable;
    }

    @Override
    public ResponseEntity<?> getRelatedProducts(Integer productId) {
        Optional<Products> optionalProducts = this.productRepository.findById(productId);
        if (optionalProducts.isEmpty())
            throw new BadRequestException("Không tìm thấy id sản phẩm");
        Products product = optionalProducts.get();
        List<Products> products = this.productRepository.findByStatusAndCategoryId(true, product.getCategoryId());
        products.remove(product);
        List<Integer> productIds = products.stream().map(Products::getId).collect(Collectors.toList());
        List<Image> imageProduct = this.imageRepository.findByDetailIdInAndType(productIds, 2);
        List<String> imgProduct = imageProduct.stream().map(Image::getImg).collect(Collectors.toList());
        List<ProductsDTO> productsDTOS = MapperUtil.mapList(products, ProductsDTO.class);
        productsDTOS.forEach(productsDTO -> {
            productsDTO.setImg(imgProduct);
            if (null == productsDTO.getPriceAfterSale()) {
                productsDTO.setDiscount(0.0);
            } else {
                Double discountPercent = (productsDTO.getPriceAfterSale() / productsDTO.getPriceSell()) * 100;
                productsDTO.setDiscount(discountPercent);
            }
        });
        return ResponseUtil.ok(productsDTOS);
    }

    @Override
    public ResponseEntity<?> getProductsInSameBrand(Integer productId) {
        Optional<Products> optionalProducts = this.productRepository.findById(productId);
        if (optionalProducts.isEmpty())
            throw new BadRequestException("Không tìm thấy id sản phẩm");
        Products product = optionalProducts.get();
        List<Products> products = this.productRepository.findByStatusAndBrandId(true, product.getBrandId());
        products.remove(product);
        List<Integer> productIds = products.stream().map(Products::getId).collect(Collectors.toList());
        List<Image> imageProduct = this.imageRepository.findByDetailIdInAndType(productIds, 2);
        List<String> imgProduct = imageProduct.stream().map(Image::getImg).collect(Collectors.toList());
        List<ProductsDTO> productsDTOS = MapperUtil.mapList(products, ProductsDTO.class);
        productsDTOS.forEach(productsDTO -> {
            productsDTO.setImg(imgProduct);
            if (null == productsDTO.getPriceAfterSale()) {
                productsDTO.setDiscount(0.0);
            } else {
                Double discountPercent = (productsDTO.getPriceAfterSale() / productsDTO.getPriceSell()) * 100;
                productsDTO.setDiscount(discountPercent);
            }
        });
        return ResponseUtil.ok(productsDTOS);
    }


}
