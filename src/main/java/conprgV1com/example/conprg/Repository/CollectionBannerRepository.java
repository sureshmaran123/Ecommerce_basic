package conprgV1com.example.conprg.Repository;

import conprgV1com.example.conprg.Entity.CollectionBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionBannerRepository extends JpaRepository<CollectionBanner,Long> {
}
