package app.repositories;

import app.model.Currency;
import app.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByIsoCode(String isoCode);
    Currency findByRegion(Region region);
}
