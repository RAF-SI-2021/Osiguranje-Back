package repositories;

import model.forex.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByIsoCode(String isoCode);
    Optional<Currency> findByRegion(String region);

}
