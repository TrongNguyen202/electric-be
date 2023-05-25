package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Page<Category> findAllByParentIdIsNullAndStatus(Pageable pageable, Boolean status);

    List<Category> findAllByParentIdIsNullAndStatus(Boolean status);

    List<Category> findAllByParentIdAndStatus(Integer parentId, Boolean status);

    List<Category> findByParentIdAndStatus(Integer parentId, Boolean status);

    List<Category> findAllByStatus(Boolean status);

    List<Category> findAllByStatusAndParentIdIn(Boolean status, List<Integer> parentIds);
}
