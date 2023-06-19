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
import usoft.cdm.electronics_market.entities.Brand;
import usoft.cdm.electronics_market.entities.HotBrand;
import usoft.cdm.electronics_market.entities.Products;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.CategoryDTO;
import usoft.cdm.electronics_market.model.HotBrandDTO;
import usoft.cdm.electronics_market.model.PriceRangeModel;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.repository.BrandRepository;
import usoft.cdm.electronics_market.repository.CategoryRepository;
import usoft.cdm.electronics_market.repository.HotBrandRepository;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.service.HotBrandService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class HotBrandServiceImpl implements HotBrandService {

    private final HotBrandRepository hotBrandRepository;

    private final UserService userService;
    private final BrandRepository brandRepository;

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> save(HotBrandDTO dto) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Brand> optional = brandRepository.findById(dto.getBrandId());
        if (optional.isEmpty())
            return ResponseUtil.badRequest("Không tìm thấy id thương hiệu ");
        Brand brand = optional.get();
        HotBrand hotBrand = HotBrand
                .builder()
                .brandId(dto.getBrandId())
                .imgBrand(brand.getImg())
                .name(brand.getName())
                .build();
        hotBrand.setCreatedBy(userLogin.getUsername());
        this.hotBrandRepository.save(hotBrand);
        return ResponseUtil.ok(brand);
    }

    @Override
    public Page<HotBrandDTO> findAll(Pageable pageable) {
        Page<HotBrand> hotBrands = this.hotBrandRepository.findAllByOrderByCreatedDateDesc(pageable);
        return MapperUtil.mapEntityPageIntoDtoPage(hotBrands, HotBrandDTO.class);
    }

    @Override
    public ResponseEntity<?> delete(List<Integer> hotBrandIds) {
        List<HotBrand> hotBrandList = new ArrayList<>();
        hotBrandIds.forEach(hotBrandId -> {
            Optional<HotBrand> optionalHotBrand = this.hotBrandRepository.findById(hotBrandId);
            if (optionalHotBrand.isEmpty()) {
                throw new BadRequestException("Không tìm thấy id của thương hiệu");
            }
            HotBrand hotBrand = optionalHotBrand.get();
            hotBrandList.add(hotBrand);
        });
        this.hotBrandRepository.deleteAll(hotBrandList);
        return ResponseUtil.message(Message.REMOVE);
    }

    @Override
    public ResponseEntity<?> getAllProduct(Integer brandId) {
        List<Products> products = this.productRepository.findByBrandIdAndStatus(brandId, true);

        return null;
    }

    @Override
    public ResponseEntity<?> getAllCategoryAndPrice(Integer brandId) {
        List<CategoryDTO> categoryDTOS = this.categoryRepository.getAllParentByBrand(brandId);
        List<CategoryDTO> categoryChild = this.categoryRepository.getAllChildByBrand(brandId);
        for (CategoryDTO dto : categoryDTOS) {
            List<CategoryDTO> categoryDTO = categoryChild.stream().filter(x -> Objects.equals(dto.getId(), x.getParentId())).collect(Collectors.toList());
            if (categoryDTO.size() > 0) {
                dto.setDtos(categoryDTO);
            }
        }
        List<PriceRangeModel> list = PriceRange.list;
        List<PriceRangeModel> res = new ArrayList<>();
        for (PriceRangeModel rangeModel : list) {
            Integer sumProduct = this.productRepository.sumProductForBrand(brandId, rangeModel.getPriceFrom(), rangeModel.getPriceTo());
            rangeModel.setQuantity(sumProduct);
            if (sumProduct > 0) {
                res.add(rangeModel);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("categories", categoryDTOS);
        map.put("rangePrice", res);
        return ResponseUtil.ok(map);
    }
}
