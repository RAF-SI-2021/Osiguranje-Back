package repositories;

import model.Forex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForexRepository extends JpaRepository<Forex, Long> {
//    Forex findByBaseCurrencyAndQuoteCurrency(Currency baseCurrency, Currency quoteCurrency);

    List<Forex> findForexBySymbol(String symbol);
}
