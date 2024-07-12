package conprgV1com.example.conprg.Service;

import conprgV1com.example.conprg.Entity.BannerDetails;
import conprgV1com.example.conprg.Repository.BannerDetailsRepository;
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
public class BannerDetailsService {

    @Autowired
    private BannerDetailsRepository bannerDetailsRepository;

    @Value("${upload.BannerDetailsImage}")
    private String uploadDirectory;

    @Transactional
    public BannerDetails saveBannerImage(MultipartFile file, String save, String title, String subtitle, String year) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        BannerDetails banner = new BannerDetails();
        banner.setSave(save);
        banner.setTitle(title);
        banner.setSubtitle(subtitle);
        banner.setYear(year);
        banner.setImageUrl("assets/images/" + fileName);
        BannerDetails savedBanner = bannerDetailsRepository.save(banner);

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

        return savedBanner;
    }

    public List<BannerDetails> getAllBanners() {
        return bannerDetailsRepository.findAll();
    }
    public void deleteBannerById(Long id) {
        bannerDetailsRepository.deleteById(id);
    }
}