package app.repositories;

import app.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByIsoCode(String isoCode);
    Optional<Currency> findByCountry(String country);

}
