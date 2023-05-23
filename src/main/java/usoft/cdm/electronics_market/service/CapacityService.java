package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.CapacityModel;

import java.util.List;

public interface CapacityService {
    ResponseEntity<?> getAll();

    ResponseEntity<?> getPage(Pageable pageable);

    ResponseEntity<?> save(CapacityModel dto);

    ResponseEntity<?> remove(List<Integer> ids);
}
