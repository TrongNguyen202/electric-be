package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Products;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {

    Page<Products> findAllByStatus(Boolean status, Pageable pageable);
    Optional<Products> findByIdAndStatus(Integer id, Boolean status);

    List<Products> findAllByStatus(Boolean status);

    Optional<Products> findByCodeAndStatus(String code, Boolean status);

    List<Products> findByStatusAndCategoryId(Boolean status, Integer categoryId);

    List<Products> findByStatusAndWarehouseId(Boolean status, Integer warehouseId);
}
