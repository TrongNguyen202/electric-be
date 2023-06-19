package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.config.security.JwtTokenProvider;
import usoft.cdm.electronics_market.model.EmailDTO;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.model.VerifyOTPRequest;
import usoft.cdm.electronics_market.model.user.ChangePassword;
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

    private final HotBrandService hotBrandService;


    @GetMapping("/product-category")
    public ResponseEntity<?> getAllProductAndCategoryForHome() {
        return this.productService.getAllProductAndCategoryForHome();
    }

    @PostMapping("/product")
    public ResponseEntity<?> getAllProductFromCategoryId(@RequestParam Integer categoryId, @RequestBody ProductsDTO dto, Pageable pageable) {
        return ResponseUtil.ok(this.productService.getAllProductFromCategoryId(categoryId, pageable, dto));
    }

    @PostMapping("/product-search")
    public ResponseEntity<?> getAllProductFromCategoryId(@RequestParam String name, @RequestBody ProductsDTO dto, Pageable pageable) {
        return ResponseUtil.ok(this.productService.findByBrandAndPriceAndMadeInForSearchProduct(name, pageable, dto));
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

    @GetMapping("/search-name-request")
    public ResponseEntity<?> getCateAndBrandAndPriceForSearchProduct(@RequestParam String name) {
        return this.categoryService.getCateAndBrandAndPriceForSearchProduct(name);
    }


    @GetMapping("/banner")
    public ResponseEntity<?> getBanner() {
        return ResponseUtil.ok(this.homePageService.displayBannerForHomePage());
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody VerifyOTPRequest request) {
        return this.OtpVerificationService.signUp(request);
    }

    @PostMapping("/forget-pass")
    public ResponseEntity<?> getForgetPassword(@RequestBody VerifyOTPRequest request) {
        return this.OtpVerificationService.getForgetPassword(request);
    }

    @PostMapping("/change-pass")
    public ResponseEntity<?> changePass(@RequestParam String token, @RequestBody ChangePassword dto) {
        return this.OtpVerificationService.changePassFromPhone(token, dto);
    }

    @GetMapping("/check-phone")
    public ResponseEntity<?> checkPhoneForgetPassword(@RequestParam String phone) {
        return this.OtpVerificationService.checkPhoneForgetPassword(phone);
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

    @GetMapping("/brand-category")
    public ResponseEntity<?> getAllCategoryAndPrice(@RequestParam Integer brandId) {
        return this.hotBrandService.getAllCategoryAndPrice(brandId);
    }
}
