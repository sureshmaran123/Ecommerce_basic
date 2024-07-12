package conprgV1com.example.conprg.Service;

import conprgV1com.example.conprg.Entity.CollectionBanner;
import conprgV1com.example.conprg.Entity.HomeSliderImage;
import conprgV1com.example.conprg.Repository.CollectionBannerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class CollectionBannerService {
    @Autowired
    private CollectionBannerRepository collectionBannerRepository;

    @Value("${upload.CollectionBannerImage}")
    private String uploadDirectory;

    @Transactional
    public CollectionBanner saveBannerImage(MultipartFile file, String save, String title) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        CollectionBanner image = new CollectionBanner();
        image.setSave(save);
        image.setTitle(title);
        image.setImage("assets/images/" + fileName);
        CollectionBanner savedImage = collectionBannerRepository.save(image);

        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save image file: " + fileName, e);
        }

        return savedImage;
    }
    public List<CollectionBanner> getAllProducts() {
        return collectionBannerRepository.findAll();
    }
    @Transactional
    public void deleteBannerById(Long id) {
        collectionBannerRepository.deleteById(id);
    }

    }




