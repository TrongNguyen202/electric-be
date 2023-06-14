package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.ProductPromotion;

import java.util.List;


@Repository
public interface ProductPromotionRepository extends JpaRepository<ProductPromotion, Integer> {

    @Query("SELECT pp.id FROM ProductPromotion pp WHERE pp.promotionId = :promotionId")
    List<Integer> findIdAllByPromotionId(@Param("promotionId") Integer promotionId);

    List<ProductPromotion> findAllByPromotionId(Integer promotionId);
}
