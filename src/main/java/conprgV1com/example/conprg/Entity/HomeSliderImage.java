package conprgV1com.example.conprg.Entity;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "homesliderimages")
public class HomeSliderImage {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String heading;

    private String subheading;
}
