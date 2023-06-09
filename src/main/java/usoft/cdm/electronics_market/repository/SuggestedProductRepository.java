package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.SuggestedProduct;

import java.util.Optional;

@Repository
public interface SuggestedProductRepository extends JpaRepository<SuggestedProduct, Integer> {

    Page<SuggestedProduct> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Optional<SuggestedProduct> findByProductId(Integer productId);

}
