package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.FooterPage;
import usoft.cdm.electronics_market.model.FooterModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface FooterPageRepository extends JpaRepository<FooterPage, Integer> {
    Optional<FooterPage> findByType(Integer type);
    @Query("SELECT new usoft.cdm.electronics_market.model.FooterModel (f.id, f.name, f.link) FROM FooterPage f WHERE f.type = :type")
    List<FooterModel> findAllModelByType(@Param("type") Integer type);
    @Query("SELECT new usoft.cdm.electronics_market.model.FooterModel (f.id, f.name, f.link) FROM FooterPage f WHERE f.type = :type")
    Page<FooterModel> findAllModelByType(Pageable pageable, Integer type);
    List<FooterPage> findAllByType(Integer type);
}
