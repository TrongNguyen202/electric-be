package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.FlashSale;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlashSaleRepository extends JpaRepository<FlashSale, Integer> {

    FlashSale findByProductIdAndStatus(Integer productId, Boolean status);

    Optional<FlashSale> findByIdAndStatus(Integer id, Boolean status);

    List<FlashSale> findByStatus(Boolean status);

    Page<FlashSale> findByStatus(Pageable pageable, Boolean status);

}
