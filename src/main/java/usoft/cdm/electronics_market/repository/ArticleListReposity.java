package usoft.cdm.electronics_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import usoft.cdm.electronics_market.entities.ArticleList;
import usoft.cdm.electronics_market.model.ArticleListDTO;

@Repository
public interface ArticleListReposity extends JpaRepository<ArticleList, Integer> {

    ResponseEntity<?> save(ArticleListDTO articleListDTO);
}
