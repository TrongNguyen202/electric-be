package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {

    Page<Products> findAllByStatus(Boolean status, Pageable pageable);
}
