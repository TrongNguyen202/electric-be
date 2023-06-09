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
    Optional<FooterPage> findByTypeAndIdWarehouse(Integer type, Integer idWarehouse);
    @Query("SELECT new usoft.cdm.electronics_market.model.FooterModel (f.id, f.name, f.link) FROM FooterPage f WHERE f.type = :type AND f.idWarehouse = :idWarehouse")
    List<FooterModel> findAllModelByTypeAndWarehouse(@Param("type") Integer type, @Param("idWarehouse") Integer idWarehouse);
    @Query("SELECT new usoft.cdm.electronics_market.model.FooterModel (f.id, f.name, f.link) FROM FooterPage f WHERE f.type = :type AND f.idWarehouse = :idWarehouse")
    Page<FooterModel> findAllModelByTypeAndWarehouse(Pageable pageable, @Param("type") Integer type, @Param("idWarehouse") Integer idWarehouse);
    List<FooterPage> findAllByTypeAndIdWarehouse(Integer type, Integer idWarehouse);
}
