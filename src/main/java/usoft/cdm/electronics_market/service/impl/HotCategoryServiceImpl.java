package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.Category;
import usoft.cdm.electronics_market.entities.HotCategory;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.HotCategoryDTO;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.repository.CategoryRepository;
import usoft.cdm.electronics_market.repository.HotCategoryRepository;
import usoft.cdm.electronics_market.service.HotCategoryService;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HotCategoryServiceImpl implements HotCategoryService {

    private final HotCategoryRepository hotCategoryRepository;

    private final ProductService productService;
    private final UserService userService;

    private final CategoryRepository categoryRepository;

    @Override
    public Page<HotCategoryDTO> findAll(Pageable pageable) {
        Page<HotCategory> categories = this.hotCategoryRepository.findAll(pageable);
        Page<HotCategoryDTO> categoryDTOS = MapperUtil.mapEntityPageIntoDtoPage(categories, HotCategoryDTO.class);
        for (HotCategoryDTO dto : categoryDTOS) {
            Category category = this.categoryRepository.findById(dto.getCategoryId()).orElseThrow();
            dto.setNameCategory(category.getName());

        }
        return categoryDTOS;
    }

    @Override
    public ResponseEntity<?> save(HotCategoryDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<HotCategory> hotCategoryOptional = this.hotCategoryRepository.findByCategoryId(dto.getCategoryId());
        if (hotCategoryOptional.isPresent()) {
            throw new BadRequestException("Đã có danh mục nổi bật cho danh mục này");
        }
        ProductsDTO productsDTO = this.productService.displayMaxDiscountByCategory(dto.getCategoryId());
        HotCategory hotCategory = HotCategory
                .builder()
                .categoryId(dto.getCategoryId())
                .discount(productsDTO.getDiscount())
                .build();
        hotCategory.setCreatedBy(userLogin.getUsername());
        this.hotCategoryRepository.save(hotCategory);
        return ResponseUtil.ok(hotCategory);
    }

    @Override
    public ResponseEntity<?> update(HotCategoryDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<HotCategory> hotCategoryCheck = this.hotCategoryRepository.findById(dto.getId());
        if (!Objects.equals(hotCategoryCheck.get().getCategoryId(), dto.getCategoryId())) {
            Optional<HotCategory> hotCategoryNew = this.hotCategoryRepository.findByCategoryId(dto.getCategoryId());
            if (hotCategoryNew.isPresent()) {
                throw new BadRequestException("Đã có danh mục nổi bật cho danh mục này");
            }
        }
        Optional<HotCategory> optionalHotCategory = this.hotCategoryRepository.findById(dto.getId());
        if (optionalHotCategory.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id của kho");
        }
        ProductsDTO productsDTO = this.productService.displayMaxDiscountByCategory(dto.getCategoryId());
        HotCategory category = MapperUtil.map(dto, HotCategory.class);
        category.setDiscount(productsDTO.getDiscount());
        category.setUpdatedBy(userLogin.getUsername());
        this.hotCategoryRepository.save(category);
        return ResponseUtil.ok(category);
    }

    @Override
    public ResponseEntity<?> getById(Integer hotCategoryId) {
        Optional<HotCategory> optionalHotCategory = this.hotCategoryRepository.findById(hotCategoryId);
        if (optionalHotCategory.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id của kho");
        }
        HotCategoryDTO dto = MapperUtil.map(optionalHotCategory.get(), HotCategoryDTO.class);
        Category category = this.categoryRepository.findById(dto.getCategoryId()).orElseThrow();
        dto.setNameCategory(category.getName());
        return ResponseUtil.ok(dto);
    }

    @Override
    public ResponseEntity<?> delete(List<Integer> hotCategoryIds) {
        List<HotCategory> hotCategoryList = new ArrayList<>();
        hotCategoryIds.forEach(hotCategoryId -> {
            Optional<HotCategory> optionalHotCategory = this.hotCategoryRepository.findById(hotCategoryId);
            if (optionalHotCategory.isEmpty()) {
                throw new BadRequestException("Không tìm thấy id của kho");
            }
            HotCategory hotCategory = optionalHotCategory.get();
            hotCategoryList.add(hotCategory);
        });
        this.hotCategoryRepository.deleteAll(hotCategoryList);
        return ResponseUtil.message(Message.REMOVE);
    }
}
