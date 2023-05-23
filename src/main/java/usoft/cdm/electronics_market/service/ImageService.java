package usoft.cdm.electronics_market.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    String uploadFile(MultipartFile file);

    List<String> uploadFile(List<MultipartFile> files);
}
