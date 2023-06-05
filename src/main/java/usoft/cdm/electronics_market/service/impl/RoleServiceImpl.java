package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.entities.Roles;
import usoft.cdm.electronics_market.repository.RolesRepository;
import usoft.cdm.electronics_market.service.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RolesRepository rolesRepository;

    @Override
    public boolean checkRole(int roleId, String name){
        Roles roles = rolesRepository.findById(roleId).orElse(null);
        if (roles == null)
            throw new BadRequestException("Không có role này");
        return roles.getName().equals(name);
    }
}
