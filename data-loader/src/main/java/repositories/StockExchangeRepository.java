package repositories;

import models.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface StockExchangeRepository extends JpaRepository<StockExchange, Long> {

    @Query( value = "SELECT b FROM StockExchange b WHERE b.name LIKE '%' || :marketName || '%'" )
    Collection<StockExchange> findAllByName( @Param( "marketName" ) String marketName );

    Optional<StockExchange> findByName( String name );

    Optional<StockExchange> findByMic( String mic );
}
