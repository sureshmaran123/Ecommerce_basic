package conprgV1com.example.conprg.Repository;

import conprgV1com.example.conprg.Entity.BannerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerDetailsRepository extends JpaRepository<BannerDetails, Long> {
}