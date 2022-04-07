package com.osiguranjeback.forex.repositories;

import com.osiguranjeback.forex.model.Forex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForexRepository extends JpaRepository<Forex, Long> {
    Optional<Forex> findByBaseCurrencyAndQuoteCurrency(String baseCurrency, String quoteCurrency);
}
