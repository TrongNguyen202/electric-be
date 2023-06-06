package usoft.cdm.electronics_market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usoft.cdm.electronics_market.service.RoleService;

import java.awt.print.Pageable;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
public class RoleAPI {
    private final RoleService roleService;

    @GetMapping
    private ResponseEntity<?> getAll(Pageable pageable){
        return roleService.getAll(pageable);
    }
}
