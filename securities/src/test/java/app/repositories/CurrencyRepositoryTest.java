package app.repositories;

import app.model.Currency;
import app.model.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository underTest;

    @Autowired
    private RegionRepository regionRepository;

    @Test
    void findByIsoCode() {
        /* Given. */
        final String ISO_CODE = "EUR";
        Currency currency = new Currency();
        currency.setIsoCode( ISO_CODE );

        underTest.save( currency );

        /* When. */
        Currency testCurrency = underTest.findByIsoCode( ISO_CODE );

        /* Then. */
        assertTrue( testCurrency.getIsoCode().equals( ISO_CODE ) );
    }

    @Test
    void findByRegion() {
        /* Given. */
        final Region region = new Region( "Europe", "testcode");
        Currency currency = new Currency();
        currency.setRegion( region );
        region.setCurrency( currency );

        regionRepository.save( region );
        underTest.save( currency );

        /* When. */
        Currency testCurrency = underTest.findByRegion( region );

        /* Then. */
        assertTrue( testCurrency.getRegion().equals( region ) );
    }
}