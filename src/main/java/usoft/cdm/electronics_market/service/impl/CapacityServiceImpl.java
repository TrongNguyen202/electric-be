package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.Capacity;
import usoft.cdm.electronics_market.model.CapacityModel;
import usoft.cdm.electronics_market.repository.CapacityRepository;
import usoft.cdm.electronics_market.service.CapacityService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CapacityServiceImpl implements CapacityService {
    private final CapacityRepository capacityRepository;

    @Override
    public ResponseEntity<?> getAll() {
        return ResponseUtil.ok(capacityRepository.findAll());
    }

    @Override
    public ResponseEntity<?> getPage(Pageable pageable) {
        return ResponseUtil.ok(capacityRepository.findAll(pageable));
    }

    @Override
    public ResponseEntity<?> save(CapacityModel dto) {
        Capacity capacity = new Capacity();
        if (dto.getId() != null)
            capacity = capacityRepository.findById(dto.getId()).orElse(null);
        if (capacity == null)
            throw new BadRequestException("Không tìm thấy id thương hiệu");
        capacity.setArea(dto.getArea());
        capacity.setName(dto.getName());
        capacity.setStatus(true);
        capacityRepository.save(capacity);
        return ResponseUtil.message(Message.SUCCESS);
    }

    @Override
    public ResponseEntity<?> remove(List<Integer> ids) {
        List<Capacity> list = new ArrayList<>();
        ids.forEach(x ->{
            Optional<Capacity> optional = capacityRepository.findById(x);
            if (optional.isEmpty())
                throw new BadRequestException("Không tìm thấy id thương hiệu: x");
            Capacity capacity = optional.get();
            capacity.setStatus(false);
            list.add(capacity);
        });
        capacityRepository.saveAll(list);
        return ResponseUtil.message(Message.REMOVE);
    }
}
