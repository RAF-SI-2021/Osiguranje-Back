package repositories;

import model.Future;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuturesRepository extends JpaRepository<Future, Long> {
}
