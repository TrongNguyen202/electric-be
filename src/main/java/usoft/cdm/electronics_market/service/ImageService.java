package usoft.cdm.electronics_market.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadFile(MultipartFile file);
}
