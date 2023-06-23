package usoft.cdm.electronics_market.controller;


import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.ReviewDTO;
import usoft.cdm.electronics_market.service.ReviewService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewAPI {
    private final ReviewService reviewService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {

        return this.reviewService.findAll();
    }

    @GetMapping("/all-newest")
    public ResponseEntity<?> getAllNewest() {

        return this.reviewService.findAllNewest();
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody ReviewRequestBody dto) {

        return this.reviewService.saveReview(dto.getReviewDTO(), dto.getImageList());
    }

    @DeleteMapping()
    public ResponseEntity<?> deleted(@RequestParam Integer idReview) {

        return this.reviewService.deleted(idReview);
    }

    @PostMapping("/reply")
    public ResponseEntity<?> replyReview(@RequestBody ReviewRequestBody dto) {

        return this.reviewService.replyReview(dto.getReviewDTO(), dto.getImageList());
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ReviewRequestBody {
    private ReviewDTO reviewDTO;
    private List<String> imageList;
}