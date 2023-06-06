package usoft.cdm.electronics_market.controller;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usoft.cdm.electronics_market.model.ProductsDTO;
import usoft.cdm.electronics_market.model.VerifyOTPRequest;
import usoft.cdm.electronics_market.service.CategoryService;
import usoft.cdm.electronics_market.service.HomePageService;
import usoft.cdm.electronics_market.service.ProductService;
import usoft.cdm.electronics_market.util.ResponseUtil;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/homepage")
public class HomePageAPI {

    private final ProductService productService;

    private final CategoryService categoryService;

    private final HomePageService homePageService;

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
        return ResponseUtil.ok(this.homePageService.display6ImgForHomePage());
    }

//    @PostMapping("/sign-up")
//    public ResponseEntity<String> signUp(@RequestBody VerifyOTPRequest request) {
//        String verificationId = request.getVerificationId();
//        String otpCode = request.getOtp();
//        try {
//            FirebaseAuth.getInstance().checkPhoneNumberVerification(verificationId, otpCode);
//            return "OTP verification successful!";
//        } catch (FirebaseAuthException e) {
//            return "OTP verification failed: " + e.getMessage();
//        }
//    }
}
