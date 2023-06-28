package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.ProductWarehouse;

import java.util.List;

@Repository
public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Integer> {

    List<ProductWarehouse> findAllByStatusAndWarehouseId(Integer warehouseId, Boolean status);

    List<ProductWarehouse> findAllByStatusAndProductId(Boolean status, Integer productId);
}
