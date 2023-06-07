package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.constant.PriceRange;
import usoft.cdm.electronics_market.entities.Category;
import usoft.cdm.electronics_market.entities.Image;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.BrandDTO;
import usoft.cdm.electronics_market.model.CategoryDTO;
import usoft.cdm.electronics_market.model.PriceRangeModel;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.repository.BrandRepository;
import usoft.cdm.electronics_market.repository.CategoryRepository;
import usoft.cdm.electronics_market.repository.ImageRepository;
import usoft.cdm.electronics_market.repository.ProductRepository;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    private final ImageRepository imageRepository;

    private final ProductRepository productRepository;

    private final BrandRepository brandRepository;

    private final ProductService productService;


    @Override
    public ResponseEntity<?> save(CategoryDTO dto, List<String> imgList) {
        Users userLogin = this.userService.getCurrentUser();
        String slug = TextUtil.slug(dto.getName());
        Category category = Category
                .builder()
                .avatarImg(dto.getAvatarImg())
                .iconImg(dto.getIconImg())
                .slug(slug)
                .name(dto.getName())
                .build();
        category.setStatus(true);
        category.setCreatedBy(userLogin.getUsername());
        Category category1 = this.categoryRepository.save(category);
        List<Image> images = new ArrayList<>();
        for (String img : imgList) {
            Image image = Image
                    .builder()
                    .img(img)
                    .detailId(category1.getId())
                    .type(1)
                    .build();
            images.add(image);
        }
        this.imageRepository.saveAll(images);
        return ResponseUtil.ok(category);
    }

    @Override
    public ResponseEntity<?> getAllList() {
        List<Category> categories = this.categoryRepository.findAllByStatus(true);
        List<Category> categoriesNew = new ArrayList<>();
        for (Category category : categories) {
            List<Category> categoryList = this.categoryRepository.findByParentIdAndStatus(category.getId(), true);
            if (categoryList.size() == 0) {
                categoriesNew.add(category);
            }
        }
        return ResponseUtil.ok(categoriesNew);
    }

    @Override
    public List<Category> findAllCategoryNoChild() {
        List<Category> categories = this.categoryRepository.findAllByParentIdIsNullAndStatus(true);
        List<Category> categoriesNew = new ArrayList<>();
        for (Category category : categories) {
            List<Category> categoryList = this.categoryRepository.findByParentIdAndStatus(category.getId(), true);
            if (categoryList.size() == 0) {
                categoriesNew.add(category);
            }
        }
        return categoriesNew;
    }

    @Override
    public ResponseEntity<?> displayById(Integer idCategory) {
        Optional<Category> optionalCategory = this.categoryRepository.findById(idCategory);
        if (optionalCategory.isEmpty()) {
            throw new BadRequestException("Phải có id của thể loại");
        } else {
            CategoryDTO dto = MapperUtil.map(optionalCategory.get(), CategoryDTO.class);
            List<String> imgs = this.productService.getImgs(dto.getId(), 1);
            dto.setImageList(imgs);
            return ResponseUtil.ok(dto);
        }
    }

    @Override
    public Page<CategoryDTO> findByAll(Pageable pageable) {
        Page<Category> categoryPage = this.categoryRepository.findAllByParentIdIsNullAndStatus(pageable, true);
        Page<CategoryDTO> categoryDTOS = MapperUtil.mapEntityPageIntoDtoPage(categoryPage, CategoryDTO.class);
        for (CategoryDTO dto : categoryDTOS) {
            List<Category> categoryList = this.categoryRepository.findByParentIdAndStatus(dto.getId(), true);
            dto.setCategoryList(categoryList);
        }
        return categoryDTOS;
    }

    @Override
    public ResponseEntity<?> findByAllForParentIdNull() {
        List<Category> categories = this.categoryRepository.findAllByParentIdIsNullAndStatus(true);
        return ResponseUtil.ok(categories);
    }

    @Override
    public ResponseEntity<?> displayCategoryChild(Integer categoryId, Pageable pageable) {
        List<Category> categories = this.categoryRepository.findAllByParentIdAndStatus(categoryId, true);
        List<Category> categoryChilds = this.categoryRepository.findAllByParentIdAndStatus(categoryId, true);
        List<Integer> categoryChildIds = categoryChilds.stream().map(Category::getId).collect(Collectors.toList());
        List<BrandDTO> brandDTOS;
        List<ProductsDTO> productsDTOS;
        List<PriceRangeModel> res;
        if (categoryChilds.isEmpty()) {
            brandDTOS = this.brandRepository.getAllBrandByCategoryId(categoryId);
            productsDTOS = this.productRepository.getAllMadeByProducts(categoryId);
            List<PriceRangeModel> list = PriceRange.list;
            res = new ArrayList<>();
            for (PriceRangeModel rangeModel : list) {
                Integer sumProduct = this.productRepository.sumProduct(categoryId, rangeModel.getPriceFrom(), rangeModel.getPriceTo());
                rangeModel.setQuantity(sumProduct);
                if (sumProduct > 0) {
                    res.add(rangeModel);
                }
            }
        } else {
            brandDTOS = this.brandRepository.getAllBrandByCategoryId(categoryChildIds);
            productsDTOS = this.productRepository.getAllMadeByProducts(categoryChildIds);
            List<PriceRangeModel> list = PriceRange.list;
            res = new ArrayList<>();
            for (PriceRangeModel rangeModel : list) {
                Integer sumProduct = this.productRepository.sumProduct(categoryChildIds, rangeModel.getPriceFrom(), rangeModel.getPriceTo());
                rangeModel.setQuantity(sumProduct);
                if (sumProduct > 0) {
                    res.add(rangeModel);
                }
            }
        }


        Map<String, Object> map = new HashMap<>();
        map.put("categories", categories);
        map.put("brandDTOS", brandDTOS);
        map.put("productsDTOS", productsDTOS);
        map.put("priceRange ", res);
        return ResponseUtil.ok(map);
    }

    @Override
    public ResponseEntity<?> update(CategoryDTO dto, List<String> imgList) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Category> optionalCategory = this.categoryRepository.findById(dto.getId());
        if (optionalCategory.isEmpty()) {
            throw new BadRequestException("Phải có id của thể loại");
        } else {
            String slug = TextUtil.slug(dto.getName());
            Category category = MapperUtil.map(dto, Category.class);
            category.setUpdatedBy(userLogin.getUsername());
            category.setSlug(slug);
            category.setStatus(true);
            this.categoryRepository.save(category);
            List<Image> imageList = this.imageRepository.findByDetailIdAndType(dto.getId(), 1);
            this.imageRepository.deleteAll(imageList);
            List<Image> images = new ArrayList<>();
            for (String img : imgList) {
                Image image = Image
                        .builder()
                        .img(img)
                        .detailId(dto.getId())
                        .type(1)
                        .build();
                images.add(image);
            }
            this.imageRepository.saveAll(images);
            return ResponseUtil.ok(category);
        }

    }

    @Override
    public ResponseEntity<?> saveChildCategory(CategoryDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Category> optionalCategory = this.categoryRepository.findById(dto.getId());
        if (optionalCategory.isEmpty()) {
            throw new BadRequestException("Phải có id của thể loại");
        } else {
            String slug = TextUtil.slug(dto.getName());
            Category category = Category
                    .builder()
                    .name(dto.getName())
                    .iconImg(dto.getIconImg())
                    .avatarImg(dto.getAvatarImg())
                    .parentId(dto.getId())
                    .slug(slug)
                    .build();
            category.setCreatedBy(userLogin.getUsername());
            category.setStatus(true);
            this.categoryRepository.save(category);
            return ResponseUtil.ok(category);
        }

    }

    @Override
    public ResponseEntity<?> updateChildCategory(CategoryDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Category> optionalCategory = this.categoryRepository.findById(dto.getId());
        if (optionalCategory.isEmpty()) {
            throw new BadRequestException("Phải có id của thể loại");
        } else {
            String slug = TextUtil.slug(dto.getName());
            Category category = MapperUtil.map(dto, Category.class);
            category.setParentId(optionalCategory.get().getParentId());
            category.setUpdatedBy(userLogin.getUsername());
            category.setStatus(true);
            category.setSlug(slug);
            this.categoryRepository.save(category);
            return ResponseUtil.ok(category);
        }
    }

    @Override
    public ResponseEntity<?> deleteCategoryIds(List<Integer> categoryIds) {
        List<Category> categories = new ArrayList<>();
        Users userLogin = this.userService.getCurrentUser();
        categoryIds.forEach(categoryId -> {
            Optional<Category> categoryOptional = this.categoryRepository.findById(categoryId);
            if (categoryOptional.isEmpty()) {
                throw new BadRequestException("Không có id của danh mục này");
            }
            Category category = categoryOptional.get();
            List<Category> categoryList = this.categoryRepository.findByParentIdAndStatus(categoryId, true);
            List<Products> products = this.productRepository.findByStatusAndCategoryId(true, categoryId);
            if (categoryList.size() > 0) {
                throw new BadRequestException("Đã có danh mục con không thể xóa ở danh mục " + category.getName());
            }
            if (products.size() > 0) {
                throw new BadRequestException("Đã có sản phẩm không thể xóa ở danh mục " + category.getName());
            }

            category.setStatus(false);
            category.setUpdatedBy(userLogin.getUsername());
            categories.add(category);

        });
        this.categoryRepository.saveAll(categories);
        return ResponseUtil.message(Message.REMOVE);
    }

    @Override
    public Page<CategoryDTO> searchByName(Pageable pageable, String name) {
        Page<Category> categoryPage = this.categoryRepository.searchByName(pageable, name);
        Page<CategoryDTO> categoryDTOS = MapperUtil.mapEntityPageIntoDtoPage(categoryPage, CategoryDTO.class);
        for (CategoryDTO dto : categoryDTOS) {
            List<Category> categoriesChild = this.categoryRepository.findByParentIdAndStatus(dto.getId(), true);
            dto.setCategoryList(categoriesChild);
        }
        return categoryDTOS;
    }


}
