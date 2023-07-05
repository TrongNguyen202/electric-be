package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.PosterCategory;

import java.util.List;

@Repository
public interface PosterCategoryRepository extends JpaRepository<PosterCategory, Integer> {
    List<PosterCategory> findAllByParentIdAndStatus(Integer idParent, Boolean status);
    Page<PosterCategory> findAllByParentIdAndStatus(Pageable pageable, Integer idParent, Boolean status);
}
