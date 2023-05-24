package usoft.cdm.electronics_market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import usoft.cdm.electronics_market.config.expection.BadRequestException;
import usoft.cdm.electronics_market.repository.ImageRepository;
import usoft.cdm.electronics_market.service.ImageService;
import usoft.cdm.electronics_market.util.DateUtil;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    private final Path staticPath = Paths.get("static");
    private final Path imagePath = Paths.get("upload");

    private final ImageRepository imageRepository;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
                Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
            }
            String org = file.getContentType();
            assert org != null;
            if (!org.contains("image"))
                throw new BadRequestException("Mời upload file có định dạng là ảnh");
            String filename = DateUtil.dateUpFile() + file.getOriginalFilename();
            Path path = CURRENT_FOLDER.resolve(staticPath)
                    .resolve(imagePath).resolve(filename);
            try (OutputStream os = Files.newOutputStream(path)) {
                os.write(file.getBytes());
            }
            return path.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Mời nhập file ảnh vào");
        }
    }

    @Override
    public List<String> uploadFile(List<MultipartFile> files) {
        try {
            List<String> img = new ArrayList<>();
            if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
                Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
            }
            for (MultipartFile file : files) {
                String org = file.getContentType();
                assert org != null;
                if (!org.contains("image"))
                    throw new BadRequestException("Mời upload file có định dạng là ảnh");
                String filename = DateUtil.dateUpFile() + file.getOriginalFilename();
                Path path = CURRENT_FOLDER.resolve(staticPath)
                        .resolve(imagePath).resolve(filename);
                try (OutputStream os = Files.newOutputStream(path)) {
                    os.write(file.getBytes());
                }
                img.add(path.toString());
            }
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Mời nhập file ảnh vào");
        }
    }
}
