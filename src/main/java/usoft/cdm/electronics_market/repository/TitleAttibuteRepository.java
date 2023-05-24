package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.TitleAttribute;

import java.util.List;

@Repository
public interface TitleAttibuteRepository extends JpaRepository<TitleAttribute, Integer> {

    List<TitleAttribute> findByProductId(Integer productId);
}
