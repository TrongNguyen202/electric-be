package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.constant.Message;
import usoft.cdm.electronics_market.entities.Image;
import usoft.cdm.electronics_market.entities.Review;
import usoft.cdm.electronics_market.entities.Users;
import usoft.cdm.electronics_market.model.ReviewDTO;
import usoft.cdm.electronics_market.repository.ImageRepository;
import usoft.cdm.electronics_market.repository.ReviewRepository;
import usoft.cdm.electronics_market.repository.UserRepository;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.service.ReviewService;
import usoft.cdm.electronics_market.service.UserService;
import usoft.cdm.electronics_market.util.MapperUtil;
import usoft.cdm.electronics_market.util.ResponseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;

    private final UserService userService;

    private final ProductService productService;

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> saveReview(ReviewDTO dto, List<String> imgs) {
        Users userLogin = this.userService.getCurrentUser();
        if (userLogin.getRoleId() == 1 || userLogin.getRoleId() == 2) {
            throw new BadRequestException("Chỉ khách hàng mới được bình luận");
        }
        if (dto.getProductId() == null) {
            throw new BadRequestException("Phải có sản phẩm để bình luận");
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
    public ResponseEntity<?> replyReview(ReviewDTO dto, List<String> imgs) {
        Users userLogin = this.userService.getCurrentUser();
        Optional<Review> optionalReview = this.reviewRepository.findAllByIdAndStatus(dto.getId(), true);
        if (optionalReview.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id của đánh giá này");
        }
        if (dto.getProductId() == null) {
            throw new BadRequestException("Phải có sản phẩm để bình luận");
        }
        Review review = Review
                .builder()
                .status(true)
                .userId(userLogin.getId())
                .content(dto.getContent())
                .parentId(dto.getId())
                .productId(dto.getProductId())
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
    public ResponseEntity<?> findAll(Integer idProduct) {
        Pageable topTen = PageRequest.of(0, 10);
        List<Review> reviews = this.reviewRepository.findAllByStatusAndParentIdIsNullAndProductId(true, idProduct, topTen);
        List<ReviewDTO> reviewDTOS = MapperUtil.mapList(reviews, ReviewDTO.class);
        for (ReviewDTO dto : reviewDTOS) {
            List<Review> list = this.reviewRepository.findAllByStatusAndParentId(true, dto.getId());
            List<ReviewDTO> dtos = MapperUtil.mapList(list, ReviewDTO.class);
            Optional<Users> usersOptional = this.userRepository.findByIdAndStatus(dto.getUserId(), true);
            if (usersOptional.isPresent()) {
                dto.setNameUser(usersOptional.get().getUsername());
            }
            for (ReviewDTO reviewDTO : dtos) {
                List<String> imgs = this.productService.getImgs(reviewDTO.getId(), 4);
                reviewDTO.setImgs(imgs);
                Optional<Users> users = this.userRepository.findByIdAndStatus(reviewDTO.getUserId(), true);
                if (users.isPresent()) {
                    reviewDTO.setNameUser(users.get().getUsername());
                }
            }
            dto.setReviewDTOS(dtos);
            List<String> imgs = this.productService.getImgs(dto.getId(), 4);
            dto.setImgs(imgs);
        }
        return ResponseUtil.ok(reviewDTOS);
    }

    @Override
    public ResponseEntity<?> findAllNewest() {
        Pageable topTen = PageRequest.of(0, 10);
        List<Review> reviews = this.reviewRepository.findAllByStatusAndParentIdIsNullOrderByCreatedDateDesc(true, topTen);
        List<ReviewDTO> reviewDTOS = MapperUtil.mapList(reviews, ReviewDTO.class);
        for (ReviewDTO dto : reviewDTOS) {
            List<Review> list = this.reviewRepository.findAllByStatusAndParentId(true, dto.getId());
            List<ReviewDTO> dtos = MapperUtil.mapList(list, ReviewDTO.class);
            for (ReviewDTO reviewDTO : dtos) {
                List<String> imgs = this.productService.getImgs(reviewDTO.getId(), 4);
                reviewDTO.setImgs(imgs);
            }
            dto.setReviewDTOS(dtos);
            List<String> imgs = this.productService.getImgs(dto.getId(), 4);
            dto.setImgs(imgs);
        }
        return ResponseUtil.ok(reviewDTOS);
    }

    @Override
    public ResponseEntity<?> deleted(Integer idReview) {
        Users userLogin = this.userService.getCurrentUser();
        if (userLogin.getRoleId() == 3) {
            throw new BadRequestException("Bạn không có quyền xóa bình luận");
        }
        Optional<Review> optionalReview = this.reviewRepository.findAllByIdAndStatus(idReview, true);
        if (optionalReview.isEmpty()) {
            throw new BadRequestException("Không tìm thấy id của đánh giá này");
        }
        Review review = optionalReview.get();
        List<Review> reviews = this.reviewRepository.findAllByStatusAndParentId(true, idReview);
        if (reviews.size() > 0) {
            for (Review re : reviews) {
                Optional<Review> check = this.reviewRepository.findAllByIdAndStatus(re.getId(), true);
                if (optionalReview.isEmpty()) {
                    throw new BadRequestException("Không tìm thấy id của đánh giá này");
                }
                check.get().setStatus(false);
                this.reviewRepository.save(check.get());
            }
        }
        review.setStatus(false);
        this.reviewRepository.save(review);
        return ResponseUtil.message(Message.REMOVE);
    }
}
