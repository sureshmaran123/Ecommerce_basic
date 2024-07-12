package conprgV1com.example.conprg.Service;



import conprgV1com.example.conprg.Entity.Product;
import conprgV1com.example.conprg.Entity.ProductImage;
import conprgV1com.example.conprg.Entity.ProductVariant;
import conprgV1com.example.conprg.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    @Value("${upload.path}")
    private String uploadPath; // Path where images will be stored

    @Value("${upload.path2}")
    private String uploadPath2; // Path for the second folder

    // Counter for generating sequential IDs
    private int imageCounter = 0;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product createProduct(Product product, List<MultipartFile> productImages) {
        List<ProductImage> images = new ArrayList<>();

        for (MultipartFile file : productImages) {
            if (file == null || file.getOriginalFilename() == null) {
                logger.error("File or original filename is null");
                continue;
            }

            try {
                String uniqueName = generateUniqueName(); // Generate a unique name for the image
                String newFileName = uniqueName + getFileExtension(file.getOriginalFilename()); // Append the file extension

                Path path1 = Paths.get(uploadPath + newFileName);
                Path path2 = Paths.get(uploadPath2 + newFileName);

                Files.copy(file.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(file.getInputStream(), path2, StandardCopyOption.REPLACE_EXISTING);

                ProductImage productImage = new ProductImage();
                productImage.setSrc("/assets/images/" + newFileName);
                productImage.setAlt(newFileName); // You can set alt text based on your requirement

                images.add(productImage);
            } catch (IOException e) {
                logger.error("Failed to save image file", e); // Use logging instead of printStackTrace
            }
        }

        product.setImages(images);

        List<ProductVariant> variants = product.getVariants();
        for (ProductVariant variant : variants) {
            if (variant.getImage_id() == null) {
                int index = variants.indexOf(variant);
                if (index < images.size()) {
                    variant.setImage_id(images.get(index));
                } else {
                    variant.setImage_id(null);
                }
            }
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product updatedProduct, List<MultipartFile> productImages) {
        // Retrieve the existing product from the database
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        // Update basic product information
        existingProduct.setTitle(updatedProduct.getTitle());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setType(updatedProduct.getType());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setCollection(updatedProduct.getCollection());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setSale(updatedProduct.isSale());
        existingProduct.setDiscount(updatedProduct.getDiscount());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setNew(updatedProduct.isNew());
        existingProduct.setTags(updatedProduct.getTags());

        // Handle product images
        if (productImages != null && !productImages.isEmpty()) {
            List<ProductImage> images = uploadProductImages(productImages);
            existingProduct.getImages().clear(); // Clear existing images
            existingProduct.getImages().addAll(images); // Add new images
        }

        // Update variants
        updateVariants(existingProduct, updatedProduct);

        // Save the updated product
        return productRepository.save(existingProduct);
    }

    private void updateVariants(Product existingProduct, Product updatedProduct) {
        List<ProductVariant> existingVariants = existingProduct.getVariants();
        List<ProductVariant> updatedVariants = updatedProduct.getVariants();
        List<ProductImage> existingImages = existingProduct.getImages();

        existingVariants.clear();
        int imageIndex = 0;

        // Iterate over updated variants
        for (ProductVariant updatedVariant : updatedVariants) {
            ProductVariant newVariant = new ProductVariant();
            newVariant.setSku(updatedVariant.getSku());
            newVariant.setSize(updatedVariant.getSize());
            newVariant.setColor(updatedVariant.getColor());

            // Assign the correct image_id
            if (updatedVariant.getImage_id() == null && !existingImages.isEmpty()) {
                newVariant.setImage_id(existingImages.get(imageIndex % existingImages.size()));
                imageIndex++; // Move to the next image for the next variant
            } else {
                newVariant.setImage_id(updatedVariant.getImage_id());
            }

            existingVariants.add(newVariant);
        }
    }

    private List<ProductImage> uploadProductImages(List<MultipartFile> productImages) {
        List<ProductImage> images = new ArrayList<>();
        for (MultipartFile file : productImages) {
            if (file == null || file.getOriginalFilename() == null) {
                logger.error("File or original filename is null");
                continue;
            }

            try {
                String uniqueName = generateUniqueName(); // Generate a unique name for the image
                String newFileName = uniqueName + getFileExtension(file.getOriginalFilename()); // Append the file extension

                Path path1 = Paths.get(uploadPath + newFileName);
                Path path2 = Paths.get(uploadPath2 + newFileName);

                Files.copy(file.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(file.getInputStream(), path2, StandardCopyOption.REPLACE_EXISTING);

                ProductImage productImage = new ProductImage();
                productImage.setSrc("/assets/images/" + newFileName);
                productImage.setAlt(newFileName); // You can set alt text based on your requirement

                images.add(productImage);
            } catch (IOException e) {
                logger.error("Failed to save image file", e); // Use logging instead of printStackTrace
            }
        }

        return images;
    }

    private String generateUniqueName() {
        // Generate a unique ID based on timestamp and a sequential number
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")); // Timestamp
        String sequentialNumber = String.format("%03d", getNextImageCounter()); // Sequential number with leading zeros
        return timestamp + sequentialNumber;
    }

    private synchronized int getNextImageCounter() {
        // Increment and return the counter value
        return ++imageCounter;
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex);
    }
}