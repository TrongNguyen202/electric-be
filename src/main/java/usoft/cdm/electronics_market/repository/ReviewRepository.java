package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Optional<Review> findAllByIdAndStatus(Integer id, Boolean status);

    List<Review> findAllByStatusAndParentIdIsNullAndProductId(Boolean status, Integer idProduct, Pageable pageable);

    List<Review> findAllByStatusAndParentIdIsNullOrderByCreatedDateDesc(Boolean status, Pageable pageable);

    List<Review> findAllByStatusAndParentId(Boolean status, Integer parentId);

}
