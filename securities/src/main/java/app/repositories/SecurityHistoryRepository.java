package app.repositories;

import app.model.SecurityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityHistoryRepository extends JpaRepository<SecurityHistory, Long> {
}
