package repositories;

import model.SecurityHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityHistoryRepository extends JpaRepository<SecurityHistory, Long> {
}
