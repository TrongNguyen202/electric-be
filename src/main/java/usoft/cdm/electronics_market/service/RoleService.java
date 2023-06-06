package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;

import java.awt.print.Pageable;

public interface RoleService {
    ResponseEntity<?> getAll(Pageable pageable);

    boolean checkRole(int roleId, String name);
}
