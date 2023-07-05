package usoft.cdm.electronics_market.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.Poster;
import usoft.cdm.electronics_market.model.Poster.PosterList;

import java.util.List;

@Repository
public interface PosterRepository extends JpaRepository<Poster, Integer> {
    @Query("SELECT new usoft.cdm.electronics_market.model.Poster.PosterList" +
            " (p.id, p.name, p.title, p.postDate, p.createdBy, p.status)" +
            " FROM Poster p WHERE p.type = :type AND p.status = :status")
    List<PosterList> findAllByTypeAndStatus(
            @Param("type") Integer type,
            @Param("status") Integer status);
    Page<Poster> findAllByTypeAndStatus(Pageable pageable, Integer type, Integer status);
}
