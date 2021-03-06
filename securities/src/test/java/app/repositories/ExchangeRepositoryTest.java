package app.repositories;

import app.model.Exchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ExchangeRepositoryTest {

    @Autowired
    private ExchangeRepository underTest;

    @Test
    void findByMIC() {
        /* Given. */
        final String MIC = "XNYS-TEST";
        Exchange exchange = new Exchange();
        exchange.setMIC( MIC );

        underTest.save( exchange );

        /* When. */
        Exchange testExchange = underTest.findByMIC( MIC );

        /* Then. */
        assertEquals( testExchange.getMIC(), MIC );
    }

    @Test
    void findByAcronym() {
        /* Given. */
        final String ACRONYM = "NYSE-TETS";
        Exchange exchange = new Exchange();
        exchange.setAcronym( ACRONYM );

        underTest.save( exchange );

        /* When. */
        Exchange testExchange = underTest.findByAcronym( ACRONYM );

        /* Then. */
        assertEquals( testExchange.getAcronym(), ACRONYM );
    }
}