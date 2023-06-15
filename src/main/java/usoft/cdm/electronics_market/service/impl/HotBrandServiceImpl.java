package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.Brand;
import usoft.cdm.electronics_market.entities.HotBrand;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.HotBrandDTO;
import usoft.cdm.electronics_market.repository.BrandRepository;
import usoft.cdm.electronics_market.repository.HotBrandRepository;
import usoft.cdm.electronics_market.service.HotBrandService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class HotBrandServiceImpl implements HotBrandService {

    private final HotBrandRepository hotBrandRepository;

    private final UserService userService;
    private final BrandRepository brandRepository;

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
}
