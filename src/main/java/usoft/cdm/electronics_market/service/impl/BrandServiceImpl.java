package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.Brand;
import usoft.cdm.electronics_market.entities.Image;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.BrandDTO;
import usoft.cdm.electronics_market.model.ImageDTO;
import usoft.cdm.electronics_market.repository.BrandRepository;
import usoft.cdm.electronics_market.repository.ImageRepository;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.service.BrandService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    private final UserService userService;

    @Override
    public ResponseEntity<?> getAll(String type) {
        return ResponseUtil.ok(brandRepository.findAllByType(type));
    }

    @Override
    public ResponseEntity<?> getAllList() {
        return ResponseUtil.ok(this.brandRepository.findAllByStatus(true));
    }

    @Override
    public ResponseEntity<?> getById(Integer id) {
        Optional<Brand> optional = brandRepository.findById(id);
        if (optional.isEmpty())
            return ResponseUtil.badRequest("Không tìm thấy id thương hiệu");
        BrandDTO dto = MapperUtil.map(optional.get(), BrandDTO.class);
        List<Image> list = this.imageRepository.findByDetailIdAndType(id, 3);
        List<ImageDTO> dtos = MapperUtil.mapList(list, ImageDTO.class);
        dto.setImageDTOS(dtos);
        return ResponseUtil.ok(dto);
    }

    @Override
    public ResponseEntity<?> search(String name, Pageable pageable) {
        return ResponseUtil.ok(brandRepository.findAllByStatusAndNameContaining(true, name, pageable));
    }

    @Override
    public ResponseEntity<?> searchForHotBrand(String name) {
        return ResponseUtil.ok(brandRepository.findAllByStatusAndNameContaining(true, name));
    }

    @Override
    public ResponseEntity<?> getPage(Pageable pageable) {
        return ResponseUtil.ok(brandRepository.findAll(pageable));
    }

    @Override
    public ResponseEntity<?> save(BrandDTO dto, List<String> images) {
        Users userLogin = this.userService.getCurrentUser();
        Brand brandCheck = brandRepository.findByNameAndStatus(dto.getName(), true);
        if (brandCheck != null) {
            return ResponseUtil.badRequest(brandCheck.getId(), "Đã có thương hiệu này!");
        }

        Brand brand = Brand
                .builder()
                .img(dto.getImg())
                .name(dto.getName())
                .type(dto.getType())
                .status(true)
                .information(dto.getInformation())
                .build();
        brand.setCreatedBy(userLogin.getUsername());
        Brand brandSave = this.brandRepository.save(brand);
        List<Image> imageList = new ArrayList<>();
        for (String imageString : images) {
            Image image = Image
                    .builder()
                    .type(3)
                    .detailId(brandSave.getId())
                    .img(imageString)
                    .build();
            imageList.add(image);
        }
        this.imageRepository.saveAll(imageList);
        BrandDTO brandDTO = MapperUtil.map(brandSave, BrandDTO.class);
        List<ImageDTO> dtos = MapperUtil.mapList(imageList, ImageDTO.class);
        brandDTO.setImageDTOS(dtos);
        return ResponseUtil.ok(brandDTO);
    }

    @Override
    public ResponseEntity<?> update(BrandDTO dto, List<ImageDTO> images) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Brand> optional = brandRepository.findById(dto.getId());
        if (optional.isEmpty())
            return ResponseUtil.badRequest("Không tìm thấy id thương hiệu");
        if (!optional.get().getName().equalsIgnoreCase(dto.getName())) {
            Brand brandCheck = brandRepository.findByNameAndStatus(dto.getName(), true);
            if (brandCheck != null)
                return ResponseUtil.badRequest(brandCheck.getId(), "Đã có thương hiệu này!");
        }
        Brand brand = MapperUtil.map(dto, Brand.class);
        brand.setStatus(true);
        brand.setUpdatedBy(userLogin.getUsername());
        this.brandRepository.save(brand);
        List<Integer> imageIdAll = this.imageRepository.findIdAllByImageId(brand.getId());
        List<Image> list = new ArrayList<>();
        for (ImageDTO imageDTO : images) {
            if (imageDTO.getId() != null) {
                imageIdAll.remove(imageDTO.getId());
                Optional<Image> image = this.imageRepository.findById(imageDTO.getId());
                Image image1 = image.get();
                image1.setImg(imageDTO.getImg());
                image1.setDetailId(brand.getId());
                image1.setType(3);
                list.add(image1);
            } else {
                Image image = Image
                        .builder()
                        .img(imageDTO.getImg())
                        .detailId(dto.getId())
                        .type(3)
                        .build();
                list.add(image);
            }
        }
        this.imageRepository.saveAll(list);
        this.imageRepository.deleteAllById(imageIdAll);
        return ResponseUtil.ok(brand);
    }

    @Override
    public ResponseEntity<?> remove(List<Integer> ids) {
        List<Brand> list = new ArrayList<>();
        for (Integer x : ids) {
            Optional<Brand> optional = brandRepository.findById(x);
            if (optional.isEmpty())
                return ResponseUtil.badRequest("Không tìm thấy id thương hiệu: x");
            Brand brand = optional.get();
            if (!productRepository.findByBrandId(brand.getId()).isEmpty())
                return ResponseUtil.badRequest("Thương hiệu " + brand.getName() + "đã có sản phẩm");
            list.add(brand);
        }
        brandRepository.deleteAll(list);
        return ResponseUtil.message(Message.REMOVE);
    }
}
