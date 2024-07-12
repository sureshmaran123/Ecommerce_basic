package conprgV1com.example.conprg.Repository;

import conprgV1com.example.conprg.Entity.HomeSliderImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;

@Repository
public interface HomeSliderImageRepository extends JpaRepository<HomeSliderImage, Long> {
}


