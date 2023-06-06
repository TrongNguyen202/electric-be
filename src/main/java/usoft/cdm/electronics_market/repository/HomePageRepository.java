package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.HomePage;

import java.util.List;

@Repository
public interface HomePageRepository extends JpaRepository<HomePage, Integer> {

    Page<HomePage> findAllByTypeGreaterThanEqual(Integer type, Pageable pageable);

    List<HomePage> findAllByTypeGreaterThanEqual(Integer type);

    List<HomePage> findAllByType(Integer type);
}
