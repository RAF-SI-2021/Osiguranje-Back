package app.repositories;

import app.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository  extends JpaRepository<Region, Long> {
    Region findByCode(String regionCode);
    Region findByName(String name);
}
