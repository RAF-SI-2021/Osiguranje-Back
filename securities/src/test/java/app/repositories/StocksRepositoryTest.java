package app.repositories;

import app.model.Exchange;
import app.model.Stock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StocksRepositoryTest {

    @Autowired
    private StocksRepository underTest;
    @Autowired
    private ExchangeRepository exchangeRepository;

    private Exchange exchange;

    @BeforeEach
    public void setup() {
        underTest.deleteAll();
        exchange = new Exchange( "New York Stock Exchange", "NYSE", "XNYS", "−5", "09:30", "16:00" );
        exchangeRepository.save( exchange );
        // exchangeRepository.save( new Exchange( "Nasdaq", "NASDAQ", "XNAS", "−5", "09:30", "16:00" ) );
    }

    private Stock generateNextStock(String suffix ) {
        Random random = new Random();
        Stock stock =
                new Stock( "symbol" + suffix,
                        "desc" + suffix,
                        exchange,
                        "update" + suffix,
                        new BigDecimal( random.nextDouble() ),
                        new BigDecimal( random.nextDouble() ),
                        new BigDecimal( random.nextDouble() ),
                        new BigDecimal( random.nextDouble() ),
                        random.nextLong(),
                        random.nextLong(),
                        new BigDecimal( random.nextDouble() ) );

        return stock;
    }

    @Test
    void findAll() {
        /* Given. */
        List<Stock> stocks = new ArrayList<>();
        final int STOCK_COUNT = 20;

        for( int i = 0; i < STOCK_COUNT; i++ )
            stocks.add( generateNextStock( String.valueOf( i ) ) );

        underTest.saveAll( stocks );

        /* When. */
        List<Stock> testStocks = underTest.findAll();

        /* Then. */
        assertEquals( stocks, testStocks );
        stocks.add( generateNextStock( String.valueOf( STOCK_COUNT + 1 ) ) );
        assertNotEquals( stocks, testStocks );
    }

    @Test
    void save() {
        /* Given. */
        Stock stock = generateNextStock( "1" );

        /* When. */
        underTest.save( stock );
        Optional<Stock> testStock = underTest.findById( stock.getId() );

        /* Then. */
        assertTrue( testStock.isPresent() );
    }

    @Test
    void findStockByTicker() {
        /* Given. */
        final String TICKER = "TT1";
        final int STOCK_COUNT = 20;

        List<Stock> stocks = new ArrayList<>();
        for( int i = 0; i < STOCK_COUNT; i++ ) {
            Stock s = generateNextStock( String.valueOf( i ) );
            s.setTicker( TICKER );
            stocks.add( s );
        }

        underTest.saveAll( stocks );

        /* When. */
        List<Stock> testStock = underTest.findStockByTicker( TICKER );

        /* Then. */
        assertEquals( stocks, testStock );
    }
}