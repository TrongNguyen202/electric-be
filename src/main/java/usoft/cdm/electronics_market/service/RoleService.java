package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    ResponseEntity<?> getAll(Pageable pageable);

    boolean checkRole(int roleId, String name);
}
