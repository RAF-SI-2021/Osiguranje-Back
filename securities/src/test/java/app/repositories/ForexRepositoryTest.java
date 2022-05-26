package app.repositories;

import app.model.Forex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ForexRepositoryTest {

    @Autowired
    private ForexRepository underTest;

    @Test
    void findForexByTicker() {
        /* Given. */
        final String TICKER = "FF1";
        Forex forex = new Forex();
        forex.setTicker( TICKER );

        underTest.save( forex );

        /* When. */
        Forex testForex = underTest.findForexByTicker( TICKER );

        /* Then. */
        assertEquals( testForex.getTicker(), TICKER );
    }
}