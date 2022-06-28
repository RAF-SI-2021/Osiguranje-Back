package buyingmarket.repositories;

import buyingmarket.model.Actuary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActuaryRepository extends JpaRepository<Actuary, Long> {
    Optional<Actuary> findActuaryByUserId(Long userId);
}
