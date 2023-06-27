package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Warehouse;

import java.util.List;
import java.util.Optional;


@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {

    Page<Warehouse> findAllByStatus(Boolean status, Pageable pageable);

    Page<Warehouse> findAllByStatusAndName(Boolean status, String name, Pageable pageable);

    Optional<Warehouse> findAllByStatusAndName(Boolean status, String name);

    List<Warehouse> findAllByStatus(Boolean status);
}
