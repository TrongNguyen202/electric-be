package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.Brand;
import usoft.cdm.electronics_market.model.BrandDTO;
import usoft.cdm.electronics_market.repository.BrandRepository;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.service.BrandService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

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
        return ResponseUtil.ok(brandRepository.findById(id));
    }

    @Override
    public ResponseEntity<?> search(String name, Pageable pageable) {
        return ResponseUtil.ok(brandRepository.findAllByStatusAndNameContaining(true, name, pageable));
    }

    @Override
    public ResponseEntity<?> getPage(Pageable pageable) {
        return ResponseUtil.ok(brandRepository.findAll(pageable));
    }

    @Override
    public ResponseEntity<?> save(BrandDTO dto) {
        Brand brand = new Brand();
        if (dto.getId() != null)
            brand = brandRepository.findById(dto.getId()).orElse(null);
        if (brand == null)
            return ResponseUtil.badRequest("Không tìm thấy id thương hiệu!");
        brand.setImg(dto.getImg());
        brand.setName(dto.getName());
        brand.setType(dto.getType());
        brand.setStatus(true);
        return ResponseUtil.ok(brandRepository.save(brand));
    }

    @Override
    public ResponseEntity<?> remove(List<Integer> ids) {
        List<Brand> list = new ArrayList<>();
        for (Integer x : ids) {
            Optional<Brand> optional = brandRepository.findById(x);
            if (optional.isEmpty())
                return ResponseUtil.badRequest("Không tìm thấy id thương hiệu: x");
            Brand brand = optional.get();
            if (productRepository.findByBrandId(brand.getId()).isPresent())
                return ResponseUtil.badRequest("Thương hiệu " + brand.getName() + "đã có sản phẩm");
            list.add(brand);
        }
        brandRepository.deleteAll(list);
        return ResponseUtil.message(Message.REMOVE);
    }
}
