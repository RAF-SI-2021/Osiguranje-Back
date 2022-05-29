package app.repositories;

import app.model.Forex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForexRepository extends JpaRepository<Forex, Long> {
//    Forex findByBaseCurrencyAndQuoteCurrency(Currency baseCurrency, Currency quoteCurrency);

    Forex findForexByTicker(String ticker);
}
