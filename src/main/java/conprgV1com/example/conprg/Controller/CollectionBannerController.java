package conprgV1com.example.conprg.Controller;

import conprgV1com.example.conprg.Entity.CollectionBanner;
import conprgV1com.example.conprg.Entity.HomeSliderImage;
import conprgV1com.example.conprg.Service.CollectionBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/collectionbanner")
public class CollectionBannerController {

    @Autowired
    private CollectionBannerService collectionBannerService;

    @PostMapping("/upload")
       public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("save") String save,
                                         @RequestParam("title") String title) {
        try {
            CollectionBanner banner = collectionBannerService.saveBannerImage(file, save, title);
            return ResponseEntity.ok().body("Image uploaded successfully. Image URL: " + banner.getImage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }
    @GetMapping("/images")
       public ResponseEntity<List<CollectionBanner>> getAllImages() {
        List<CollectionBanner> images = collectionBannerService.getAllProducts();
        return ResponseEntity.ok().body(images);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable Long id) {
        try {
            collectionBannerService.deleteBannerById(id);
            return ResponseEntity.ok().body("Collection banner with ID " + id + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete collection banner: " + e.getMessage());
        }
    }
}
