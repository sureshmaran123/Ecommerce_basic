package conprgV1com.example.conprg.Controller;

import conprgV1com.example.conprg.Entity.BannerDetails;
import conprgV1com.example.conprg.Service.BannerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")

public class BannerDetailsController {

    @Autowired
    private BannerDetailsService bannerDetailsService;

    @PostMapping("/banner-details")

    public BannerDetails uploadBannerImage(@RequestParam("file") MultipartFile file,
                                           @RequestParam("save") String save,
                                           @RequestParam("title") String title,
                                           @RequestParam("subtitle") String subtitle,
                                           @RequestParam("year") String year) throws IOException {
        return bannerDetailsService.saveBannerImage(file, save, title, subtitle, year);
    }

    @GetMapping("/banner-details")
    public List<BannerDetails> getAllBannerDetails() {
        return bannerDetailsService.getAllBanners();
    }
    @DeleteMapping("/banner-details/{id}")
    public void deleteBanner(@PathVariable Long id) {
        bannerDetailsService.deleteBannerById(id);
    }
}