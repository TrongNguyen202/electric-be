package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.EmailDTO;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.model.VerifyOTPRequest;
import usoft.cdm.electronics_market.service.*;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.mail.MessagingException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/homepage")
public class HomePageAPI {

    private final ProductService productService;

    private final CategoryService categoryService;

    private final HomePageService homePageService;

    private final OtpVerifiService OtpVerificationService;

    private final EmailService emailService;

    @GetMapping("/product-category")
    public ResponseEntity<?> getAllProductAndCategoryForHome() {
        return this.productService.getAllProductAndCategoryForHome();
    }

    @PostMapping("/product")
    public ResponseEntity<?> getAllProductFromCategoryId(@RequestParam Integer categoryId, @RequestBody ProductsDTO dto, Pageable pageable) {
        return ResponseUtil.ok(this.productService.getAllProductFromCategoryId(categoryId, pageable, dto));
    }

    @GetMapping("/category")
    public ResponseEntity<?> findByAllForParentIdNull() {
        return this.categoryService.findByAllForParentIdNull();
    }

    @GetMapping("/category-child")
    public ResponseEntity<?> getAllCategoryChild(@RequestParam Integer categoryId, Pageable pageable) {
        return this.categoryService.displayCategoryChild(categoryId, pageable);
    }

    @GetMapping("/related-product")
    public ResponseEntity<?> getRelatedProducts(@RequestParam Integer productId) {
        return this.productService.getRelatedProducts(productId);
    }


    @GetMapping("/same-brand")
    public ResponseEntity<?> getProductsInSameBrand(@RequestParam Integer productId) {
        return this.productService.getProductsInSameBrand(productId);
    }

    @GetMapping("/search-name")
    public ResponseEntity<?> searchNameForHomepage(@RequestParam String name, Pageable pageable) {
        return this.productService.searchNameForHomepage(name, pageable);
    }


    @GetMapping("/banner")
    public ResponseEntity<?> getBanner() {
        return ResponseUtil.ok(this.homePageService.displayBannerForHomePage());
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody VerifyOTPRequest request) {
        return this.OtpVerificationService.signUp(request);
    }

    @PostMapping("/signup-email")
    public ResponseEntity<?> signUpEmail(@RequestParam String email) {
        return this.emailService.sendGmailForSignUp(email);
    }

    @PostMapping("/signup-email-otp")
    public ResponseEntity<?> signUpEmail(@RequestBody EmailDTO dto) {
        return this.emailService.checkOTPForSignUp(dto);
    }

    @PostMapping("/send-again-otp")
    public ResponseEntity<?> sendAgainOTP(@RequestParam String email) {
        return this.emailService.sendAgainOTP(email);
    }

}
