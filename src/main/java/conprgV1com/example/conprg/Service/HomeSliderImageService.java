package conprgV1com.example.conprg.Service;

import conprgV1com.example.conprg.Entity.HomeSliderImage;
import conprgV1com.example.conprg.Entity.Product;
import conprgV1com.example.conprg.Repository.HomeSliderImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class HomeSliderImageService {

    @Autowired
    private HomeSliderImageRepository homeSliderImageRepository;

    @Value("${upload.HomeSliderImage}")
    private String uploadDirectory;

    @Transactional
    public HomeSliderImage saveImage(MultipartFile file, String heading, String subheading) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        HomeSliderImage image = new HomeSliderImage();
        image.setHeading(heading);
        image.setSubheading(subheading);
        image.setImageUrl("assets/images/" + fileName);

        HomeSliderImage savedImage = homeSliderImageRepository.save(image);

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
    public List<HomeSliderImage> getAllProducts() {
        return homeSliderImageRepository.findAll();
    }

    @Transactional
    public void deleteslider(Long id) {
        homeSliderImageRepository.deleteById(id);
    }
}