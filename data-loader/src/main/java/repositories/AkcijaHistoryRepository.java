package repositories;

import models.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AkcijaHistoryRepository extends JpaRepository<History, Long> {
}
