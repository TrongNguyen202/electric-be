package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import usoft.cdm.electronics_market.repository.ImageRepository;
import usoft.cdm.electronics_market.service.ImageService;
import usoft.cdm.electronics_market.util.DateUtil;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    private final Path staticPath = Paths.get("static");
    private final Path imagePath = Paths.get("upload");

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
                Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
            }
            String filename = DateUtil.dateUpFile() + file.getOriginalFilename();
            Path path = CURRENT_FOLDER.resolve(staticPath)
                    .resolve(imagePath).resolve(filename);
            try (OutputStream os = Files.newOutputStream(path)) {
                os.write(file.getBytes());
            }
            return path.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
