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
import usoft.cdm.electronics_market.model.*;
import usoft.cdm.electronics_market.repository.*;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;
import usoft.cdm.electronics_market.util.TextUtil;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

    private final FlashSaleRepository flashSaleRepository;

    private final ProductWarehouseRepository productWarehouseRepository;


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
            FlashSale flashSale = this.flashSaleRepository.findByProductIdAndStatus(productsDTO.getId(), true);
            if (flashSale != null) {
                productsDTO.setPriceFlashSale(flashSale.getPriceFlashSale());
                productsDTO.setEndFlashSale(flashSale.getEndSale());
                productsDTO.setQuantitySale(flashSale.getQuantitySale());
            }
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

            setDiscount(productsDTO);
            if (productsDTO.getQuantity() > 0) {
                productsDTO.setCondition("Còn hàng");
            } else if (null == productsDTO.getQuantity()) {
                productsDTO.setCondition("Hết hàng");
            } else {
                productsDTO.setCondition("Hết hàng");
            }

            productsDTO.setDto(titleAttributeDTOS);
            return ResponseUtil.ok(productsDTO);
        }
    }

    @Override
    public ResponseEntity<?> save(ProductsDTO dto, List<String> imgList, List<TitleAttributeDTO> titleAttributeDTOs) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Products> productCheck = this.productRepository.findByCodeAndStatusAndNameAndPriceAfterSale(dto.getCode(), true, dto.getName(), dto.getPriceAfterSale());
        if (productCheck.isPresent()) {
            throw new BadRequestException("Sản phẩm đã tồn tại");
        }

        if (dto.getQuantityImport() <= 0) {
            throw new BadRequestException("Phải nhập số lượng nhập");
        }
        Products products = Products
                .builder()
                .code(dto.getCode())
                .name(dto.getName())
                .slug(TextUtil.slug(dto.getName()))
                .brandId(dto.getBrandId())
                .categoryId(dto.getCategoryId())
                .priceImport(dto.getPriceImport())
                .priceSell(dto.getPriceSell())
                .priceAfterSale(dto.getPriceAfterSale())
                .information(dto.getInformation())
                .quantity(dto.getQuantityImport())
                .madeIn(dto.getMadeIn())
                .status(true)
                .build();
        products.setCreatedBy(userLogin.getUsername());
        Products productsSave = this.productRepository.save(products);
        ProductWarehouse productWarehouse = this.productWarehouseRepository
                .save(ProductWarehouse
                        .builder()
                        .productId(productsSave.getId())
                        .warehouseId(dto.getWarehouseId())
                        .status(true)
                        .build());
        ProductsDTO productsDTO = MapperUtil.map(products, ProductsDTO.class);
        productsDTO.setProductWarehouse(productWarehouse);
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

        return ResponseUtil.ok(productsDTO);
    }

    @Override
    public ResponseEntity<?> update(ProductsDTO dto, Integer idProductWarehouse, List<String> imgList, List<TitleAttributeDTO> titleAttributeDTOs) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Products> optionalProducts = this.productRepository.findById(dto.getId());

        if (!optionalProducts.get().getCode().equals(dto.getCode())
                && !optionalProducts.get().getName().equals(dto.getName())
                && !optionalProducts.get().getPriceImport().equals(dto.getPriceAfterSale())) {
            Optional<Products> productCheck = this.productRepository.findByCodeAndStatusAndNameAndPriceAfterSale(dto.getCode(), true, dto.getName(), dto.getPriceAfterSale());
            if (productCheck.isPresent()) {
                throw new BadRequestException("Sản phẩm đã tồn tại");
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
            Optional<ProductWarehouse> productWarehouse = this.productWarehouseRepository.findById(idProductWarehouse);
            if (optionalProducts.isEmpty()) {
                throw new BadRequestException("Không tìm thấy đại lý của sản phẩm");
            }
            ProductWarehouse warehouse = productWarehouse.get();
            warehouse.setWarehouseId(dto.getWarehouseId());
            warehouse.setProductId(products.getId());
            this.productWarehouseRepository.save(warehouse);
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
            for (TitleAttribute titleAttribute : titleAttributes) {
                List<Attribute> attributes = this.attributeRepository.findByTitleAttributeId(titleAttribute.getId());
                this.attributeRepository.deleteAll(attributes);
            }
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
        List<CategoryHomePage> categoryDTOS = this.categoryRepository.getParentIdIsNullAndStatus();
        categoryDTOS.forEach(categoryDTO -> {
            List<String> imgs = getImgs(categoryDTO.getId(), 1);
            Random random = new Random();
            int randomIndex = random.nextInt(imgs.size());
            String imgRandom = imgs.get(randomIndex);
            List<CategoryChildHomePage> categoryList = this.categoryRepository.getParentIdAndStatus(categoryDTO.getId());
            categoryDTO.setCategoryList(categoryList);
            categoryDTO.setPicCategory(imgRandom);
            if (categoryList.isEmpty()) {
                List<ProductForHomePage> dtos = this.productRepository.getProductsForHomePage(categoryDTO.getId());
                categoryDTO.setProductsDTOS(dtos);
            } else {
                for (CategoryChildHomePage category : categoryList) {
                    List<ProductForHomePage> dtos = this.productRepository.getProductsForHomePage(category.getId());
                    categoryDTO.setProductsDTOS(dtos);
                }
            }

        });
        return ResponseUtil.ok(categoryDTOS);
    }


    @Override
    public Page<ProductsDTO> getAllProductFromCategoryId(Integer categoryId, Pageable pageable, ProductsDTO dto) {
        List<Category> categories = this.categoryRepository.findAllByParentIdAndStatus(categoryId, true);
        Page<ProductsDTO> productsDTOSSearch;
        if (categories.size() > 0) {
            List<Integer> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
            productsDTOSSearch = this.productRepository.findByBrandAndPriceAndMadeIn(categoryIds, dto, pageable);

        } else {
            productsDTOSSearch = this.productRepository.findByBrandAndPriceAndMadeIn(categoryId, dto, pageable);
        }
        productsDTOSSearch.forEach(productsDTO -> {
            List<String> imgProductSearch = getImgs(productsDTO.getId(), 2);
            productsDTO.setImg(imgProductSearch);
            setDiscount(productsDTO);
        });
        return productsDTOSSearch;

    }

    @Override
    public Page<ProductsDTO> findByBrandAndPriceAndMadeInForSearchProduct(String name, Pageable
            pageable, ProductsDTO dto) {
        Page<ProductsDTO> productsDTOS = this.productRepository.findByBrandAndPriceAndMadeInForSearchProduct(name, dto, pageable);
        productsDTOS.forEach(productsDTO -> {
            List<String> imgProductSearch = getImgs(productsDTO.getId(), 2);
            productsDTO.setImg(imgProductSearch);
            setDiscount(productsDTO);
        });

        return productsDTOS;
    }

    @Override
    public Page<ProductsDTO> findByPriceAndCategory(Integer brandId, Pageable pageable, ProductsDTO dto) {
        Page<ProductsDTO> productsDTOSSearch;
        if (dto.getCategoryId() != null) {
            List<Category> categories = this.categoryRepository.findAllByParentIdAndStatus(dto.getCategoryId(), true);
            if (categories.size() > 0) {
                List<Integer> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
                productsDTOSSearch = this.productRepository.findByPriceAndCategoryList(brandId, categoryIds, dto, pageable);
            } else {
                productsDTOSSearch = this.productRepository.findByPriceAndCategory(brandId, dto, pageable);
            }
        } else {
            productsDTOSSearch = this.productRepository.findByPriceAndCategory(brandId, dto, pageable);
        }
        for (ProductsDTO productsDTO : productsDTOSSearch) {
            List<String> imgProductSearch = getImgs(productsDTO.getId(), 2);
            productsDTO.setImg(imgProductSearch);
        }
        return productsDTOSSearch;
    }

    @Override
    public ResponseEntity<?> getRelatedProducts(Integer productId) {
        Optional<Products> optionalProducts = this.productRepository.findById(productId);
        if (optionalProducts.isEmpty())
            throw new BadRequestException("Không tìm thấy id sản phẩm");
        Products product = optionalProducts.get();
        ProductsDTO dto = MapperUtil.map(product, ProductsDTO.class);
        List<ProductsDTO> productsDTOS = this.productRepository.getRelatedProducts(product.getCategoryId());
        productsDTOS.remove(dto);
        productsDTOS.forEach(productsDTO -> {
            setDiscount(productsDTO);
            productsDTO.setInformation(null);
        });
        return ResponseUtil.ok(productsDTOS);
    }

    @Override
    public ResponseEntity<?> getProductsInSameBrand(Integer productId) {
        Optional<Products> optionalProducts = this.productRepository.findById(productId);
        if (optionalProducts.isEmpty())
            throw new BadRequestException("Không tìm thấy id sản phẩm");
        Products product = optionalProducts.get();
        ProductsDTO dto = MapperUtil.map(product, ProductsDTO.class);
        List<ProductsDTO> productsDTOS = this.productRepository.getProductsInSameBrand(product.getBrandId());
        productsDTOS.remove(dto);
        productsDTOS.forEach(productsDTO -> {
            setDiscount(productsDTO);
            productsDTO.setInformation(null);
        });
        return ResponseUtil.ok(productsDTOS);
    }

    @Override
    public Page<ProductsDTO> getProductForHotBrand(Integer brandId, Pageable pageable) {
        Page<ProductsDTO> productsDTOPage = this.productRepository.getProductsInSameBrand(brandId, pageable);
        for (ProductsDTO dto : productsDTOPage) {
            setDiscount(dto);
        }
        return productsDTOPage;
    }

    @Override
    public Page<Products> searchByCategory(Integer categoryId, Pageable pageable) {
        return this.productRepository.searchByCategory(pageable, categoryId);
    }

    @Override
    public Page<Products> searchByName(String name, Pageable pageable) {
        return this.productRepository.searchByName(pageable, name);
    }

    @Override
    public List<String> getImgs(Integer id, Integer type) {
        List<Image> images = this.imageRepository.findByDetailIdAndType(id, type);
        List<String> imgs = images.stream().map(Image::getImg).collect(Collectors.toList());
        return imgs;
    }


    @Override
    public void setDiscount(ProductsDTO dto) {
        if (null == dto.getPriceAfterSale()) {
            dto.setDiscount(0.0);
        } else {
            Double discountPercent = (dto.getPriceAfterSale() / dto.getPriceSell()) * 100;
            Double discount = 100 - discountPercent;
            dto.setDiscount(discount);

        }
    }

    @Override
    public ResponseEntity<?> searchNameForHomepage(String name, Pageable pageable) {
        Page<ProductsDTO> productsDTOS = this.productRepository.searchNameForHomepage(name, pageable);
        for (ProductsDTO dto : productsDTOS) {
            setDiscount(dto);
            List<String> imgs = getImgs(dto.getId(), 2);
            dto.setImg(imgs);
        }
        return ResponseUtil.ok(productsDTOS);
    }

    @Override
    public ProductsDTO displayMaxDiscountByCategory(Integer categoryId) {
        ProductsDTO dto = this.productRepository.getDiscountByCategory(categoryId);
        return dto;
    }


}
