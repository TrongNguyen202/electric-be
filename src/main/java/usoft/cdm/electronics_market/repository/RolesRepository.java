package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Roles;


@Repository
public interface RolesRepository extends JpaRepository<Roles,Integer> {
    Roles findByName(String name);

    @Query("SELECT r FROM Roles r WHERE r.name <> 'CUSTOMER'")
    Page<Roles> getAll(Pageable pageable);
}
