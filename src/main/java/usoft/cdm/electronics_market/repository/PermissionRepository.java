package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Permission;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("SELECT p from Permission p, RolePermission rp, Roles r" +
            " WHERE rp.roleId = r.id AND rp.permissionId = p.id AND p.name = :name AND r.id = :id")
    Optional<Permission> getPer(
            @Param("id") Integer id,
            @Param("name") String name
    );
}
