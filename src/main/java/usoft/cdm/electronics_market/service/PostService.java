package usoft.cdm.electronics_market.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.Poster.PosterSaveCategory;
import usoft.cdm.electronics_market.model.Poster.SavePoster;

public interface PostService {

    ResponseEntity<?> getPosterCategory(Integer parentId);

    ResponseEntity<?> getPosterCategory(Pageable pageable);

    ResponseEntity<?> savePosterCategory(PosterSaveCategory model);

    ResponseEntity<?> deletePosterCategory(Integer id);

    ResponseEntity<?> getPoster(Integer type);

    ResponseEntity<?> getPoster(Pageable pageable, Integer type);

    ResponseEntity<?> getPosterById(Integer id);

    ResponseEntity<?> savePoster(SavePoster req);

    ResponseEntity<?> approvePoster(Integer id);

    ResponseEntity<?> deletePoster(Integer id);
}
