package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.HotCategory;

import java.util.Optional;

@Repository
public interface HotCategoryRepository extends JpaRepository<HotCategory, Integer> {

    Optional<HotCategory> findByCategoryId(Integer categoryId);

    Page<HotCategory> findAll(Pageable pageable);
}
