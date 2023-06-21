package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.entities.Image;
import usoft.cdm.electronics_market.entities.Review;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.ReviewDTO;
import usoft.cdm.electronics_market.repository.ImageRepository;
import usoft.cdm.electronics_market.repository.ProductRepository;
import usoft.cdm.electronics_market.repository.ReviewRepository;
import usoft.cdm.electronics_market.service.ReviewService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    private final UserService userService;

    @Override
    public ResponseEntity<?> saveReview(ReviewDTO dto, List<String> imgs) {
        Users userLogin = this.userService.getCurrentUser();
        if (userLogin.getRoleId() == 1 || userLogin.getRoleId() == 2) {
            throw new BadRequestException("Chỉ khách hàng mới được bình luận");
        }
        Review review = Review
                .builder()
                .content(dto.getContent())
                .productId(dto.getProductId())
                .userId(userLogin.getId())
                .vote(dto.getVote())
                .status(true)
                .build();
        Review reviewSave = this.reviewRepository.save(review);
        ReviewDTO reviewDTO = MapperUtil.map(reviewSave, ReviewDTO.class);
        List<Image> imageList = null;
        if (imgs.size() > 0) {
            List<Image> images = new ArrayList<>();
            for (String img : imgs) {
                Image image = Image
                        .builder()
                        .type(4)
                        .detailId(reviewSave.getId())
                        .img(img)
                        .build();
                images.add(image);
            }
            imageList = this.imageRepository.saveAll(images);
        }
        reviewDTO.setImageList(imageList);

        return ResponseUtil.ok(reviewDTO);
    }

    @Override
    public ResponseEntity<?> updateReview(ReviewDTO dto, List<String> imgs) {
        Users userLogin = this.userService.getCurrentUser();


        return null;
    }
}
