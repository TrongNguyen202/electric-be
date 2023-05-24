package usoft.cdm.electronics_market.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import usoft.cdm.electronics_market.service.ImageService;
import usoft.cdm.electronics_market.util.ResponseUtil;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")

public class ImageAPI {

    private final ImageService imageService;

    @PostMapping()
    public ResponseEntity<?> uploadFile(@RequestParam @NotNull MultipartFile file) {
        return ResponseUtil.ok(imageService.uploadFile(file));
    }

    @PostMapping("/img-list")
    public ResponseEntity<?> uploadFiles(@RequestParam @NotNull List<MultipartFile> files) {

        return ResponseUtil.ok(imageService.uploadFile(files));
    }
}
