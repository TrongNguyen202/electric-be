package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        Category category = Category
                .builder()
                .avatarImg(dto.getAvatarImg())
                .iconImg(dto.getIconImg())
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
    public ResponseEntity<?> update(CategoryDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Category> optionalCategory = this.categoryRepository.findById(dto.getId());
        if (optionalCategory.isEmpty()) {
            throw new BadRequestException("Phải có id của thể loại");
        } else {
            Category category = MapperUtil.map(dto, Category.class);
            category.setUpdatedBy(userLogin.getUsername());
            this.categoryRepository.save(category);
            return ResponseUtil.ok(category);
        }

    }
}
