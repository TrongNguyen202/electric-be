package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Image;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findByDetailIdAndType(Integer detailId, Integer type);

    Image findFirstByDetailIdAndType(Integer detailId, Integer type);

    @Query("SELECT i.id FROM Image i WHERE i.detailId = :detailId AND i.type = 3")
    List<Integer> findIdAllByImageId(@Param("detailId") Integer detailId);
}
