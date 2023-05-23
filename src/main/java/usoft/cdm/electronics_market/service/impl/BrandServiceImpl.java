package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.Brand;
import usoft.cdm.electronics_market.model.BrandDTO;
import usoft.cdm.electronics_market.repository.BrandRepository;
import usoft.cdm.electronics_market.service.BrandService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public ResponseEntity<?> getAll() {
        return ResponseUtil.ok(brandRepository.findAll());
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
            throw new BadRequestException("Không tìm thấy id thương hiệu");
        brand.setImg(dto.getImg());
        brand.setName(dto.getName());
        brand.setStatus(true);
        brandRepository.save(brand);
        return ResponseUtil.message(Message.SUCCESS);
    }

    @Override
    public ResponseEntity<?> remove(List<Integer> ids) {
        List<Brand> list = new ArrayList<>();
        ids.forEach(x ->{
            Optional<Brand> optional = brandRepository.findById(x);
            if (optional.isEmpty())
                throw new BadRequestException("Không tìm thấy id thương hiệu: x");
            Brand brand = optional.get();
            brand.setStatus(false);
            list.add(brand);
        });
        brandRepository.saveAll(list);
        return ResponseUtil.message(Message.REMOVE);
    }
}
