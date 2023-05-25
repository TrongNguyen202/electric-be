package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Image;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findByDetailIdAndType(Integer detailId, Integer type);

    List<Image> findByDetailIdInAndType(List<Integer> detailIds,Integer type);

}
