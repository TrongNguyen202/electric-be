package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.HotBrand;

@Repository
public interface HotBrandRepository extends JpaRepository<HotBrand, Integer> {

    Page<HotBrand> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
