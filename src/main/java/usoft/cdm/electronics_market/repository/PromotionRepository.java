package usoft.cdm.electronics_market.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Promotion;

import java.util.List;
import java.util.Optional;


@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    Optional<Promotion> findAllByIdAndStatus(Integer id, Boolean status);

    Page<Promotion> findAllByStatus(Boolean status, Pageable pageable);

    List<Promotion> findAllByStatus(Boolean status);
}
