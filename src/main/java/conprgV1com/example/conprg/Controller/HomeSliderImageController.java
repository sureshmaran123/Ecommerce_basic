package conprgV1com.example.conprg.Controller;


import conprgV1com.example.conprg.Entity.HomeSliderImage;
import conprgV1com.example.conprg.Service.HomeSliderImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/homeslider")
public class HomeSliderImageController {

    @Autowired
    private HomeSliderImageService homeSliderImageService;

    @PostMapping("/upload")
       public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("heading") String heading,
                                         @RequestParam("subheading") String subheading) {
        try {
            HomeSliderImage image = homeSliderImageService.saveImage(file, heading, subheading);
            return ResponseEntity.ok().body("Image uploaded successfully. Image URL: " + image.getImageUrl());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/images")
      public ResponseEntity<List<HomeSliderImage>> getAllImages() {
        List<HomeSliderImage> images = homeSliderImageService.getAllProducts();
        return ResponseEntity.ok().body(images);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable Long id) {
        try {
            homeSliderImageService.deleteslider(id);
            return ResponseEntity.ok().body("Collection banner with ID " + id + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete collection banner: " + e.getMessage());
        }
    }
}