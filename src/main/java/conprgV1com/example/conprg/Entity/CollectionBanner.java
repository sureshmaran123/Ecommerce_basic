package conprgV1com.example.conprg.Entity;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "collection_banner")

public class CollectionBanner {





    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;



    private String save;

    private String title;
}
