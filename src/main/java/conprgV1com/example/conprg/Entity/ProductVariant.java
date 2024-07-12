package conprgV1com.example.conprg.Entity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import conprgV1com.example.conprg.Repository.ProductImageSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long variantId;
    private String sku;
    private String size;
    private String color;

    @ManyToOne
    @JoinColumn(name = "image_id")
    @JsonSerialize(using = ProductImageSerializer.class) // Use custom serializer
    private ProductImage image_id; // Reference to ProductImage
}