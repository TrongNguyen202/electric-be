package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import usoft.cdm.electronics_market.entities.RolePermission;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {
    @Query("SELECT rp.permissionId FROM RolePermission rp WHERE rp.roleId = :roleId")
    List<Integer> getPerIdByRoleId(@Param("roleId") Integer roleId);
}
