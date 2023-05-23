package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.entities.Category;
import usoft.cdm.electronics_market.entities.Image;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.CategoryDTO;
import usoft.cdm.electronics_market.repository.CategoryRepository;
import usoft.cdm.electronics_market.repository.ImageRepository;
import usoft.cdm.electronics_market.service.CategoryService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;
import usoft.cdm.electronics_market.util.TextUtil;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    private final ImageRepository imageRepository;

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
        category.setStatus(dto.getStatus());
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
    public ResponseEntity<?> displayById(Integer idCategory) {
        Optional<Category> optionalCategory = this.categoryRepository.findById(idCategory);
        if (optionalCategory.isEmpty()) {
            throw new BadRequestException("Phải có id của thể loại");
        } else {
            CategoryDTO dto = MapperUtil.map(optionalCategory.get(), CategoryDTO.class);
            List<Image> imageList = this.imageRepository.findByDetailIdAndType(dto.getId(), 1);
            List<String> imgs = imageList.stream().map(Image::getImg).collect(Collectors.toList());
            dto.setImageList(imgs);
            return ResponseUtil.ok(dto);
        }
    }

    @Override
    public Page<CategoryDTO> findByAll(Pageable pageable) {
        Page<Category> categoryPage = this.categoryRepository.findAllByParentIdIsNull(pageable);
        List<Integer> categoryIds = categoryPage.stream().map(Category::getId).collect(Collectors.toList());
        List<Category> categoryList = this.categoryRepository.findAllByParentIdIn(categoryIds);
        Page<CategoryDTO> categoryDTOS = MapperUtil.mapEntityPageIntoDtoPage(categoryPage, CategoryDTO.class);
        for (CategoryDTO dto : categoryDTOS) {
            dto.setCategoryList(categoryList);
        }
        return categoryDTOS;
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
                    .status(dto.getStatus())
                    .build();
            category.setCreatedBy(userLogin.getUsername());
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
            category.setSlug(slug);
            this.categoryRepository.save(category);
            return ResponseUtil.ok(category);
        }
    }

}
