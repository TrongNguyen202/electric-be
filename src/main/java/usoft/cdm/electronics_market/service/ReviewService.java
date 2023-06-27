package usoft.cdm.electronics_market.service;

import org.springframework.http.ResponseEntity;
import usoft.cdm.electronics_market.model.ReviewDTO;

import java.util.List;

public interface ReviewService {

    ResponseEntity<?> saveReview(ReviewDTO dto, List<String> imgs);

    ResponseEntity<?> replyReview(ReviewDTO dto, List<String> imgs);

    ResponseEntity<?> findAll(Integer idProduct);

    ResponseEntity<?> findAllNewest();

    ResponseEntity<?> deleted(Integer idReview);
}
