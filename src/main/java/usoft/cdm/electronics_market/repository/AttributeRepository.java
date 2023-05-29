package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Attribute;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Integer> {

    List<Attribute> findByTitleAttributeId(Integer titleAttributeId);

    List<Attribute> findByTitleAttributeIdIn(List<Integer> titleAttributeId);
}
