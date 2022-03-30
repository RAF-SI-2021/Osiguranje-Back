package repositories;

import models.Akcija;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AkcijaRepository extends JpaRepository<Akcija, Long> {

}
