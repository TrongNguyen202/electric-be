package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Brand;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer>, BrandRepositoryCustom {
    List<Brand> findAllByType(String type);
    Brand findByName(String name);

    Optional<Brand> findByIdAndStatus(Integer id, Boolean status);

    List<Brand> findAllByStatus(Boolean status);

    List<Brand> findAllByStatusAndNameContaining(Boolean status, String name, Pageable pageable);
}
