package security.test;

import app.model.Currency;
import app.model.Forex;
import app.model.dto.ForexDTO;
import app.repositories.CurrencyRepository;
import app.repositories.ForexRepository;
import app.services.ForexService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ForexServiceTest {
    private ForexRepository forexRepository = mock(ForexRepository.class);
    private CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
    private static final long VALID_ID = 1;
    private static final long INVALID_ID = 2;
    private static final String symbol = "SYMBOL";
    private static final String description = "DESCRIPTION";
    private static final String lastUpdated = "LAST_UPDATED";
    private static final BigDecimal price = new BigDecimal("1.0");
    private static final BigDecimal ask = new BigDecimal("1.0");
    private static final BigDecimal bid = new BigDecimal("1.0");
    private static final BigDecimal priceChange = new BigDecimal("1.0");
    private static final Long volume = 1L;
    private static final Integer contractSize = 1;
    private static final Currency quoteCurrency = new Currency();
    private static final Currency baseCurrency = new Currency();
    private ForexService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ForexService(forexRepository, currencyRepository);
    }

    @Test
    public void findById_ReturnsForexDTO_ForValidID() {
        Forex forex = new Forex(symbol, description, lastUpdated,
                                price, ask, bid, priceChange, volume,
                                baseCurrency, quoteCurrency, contractSize);
        Optional<Forex> optional = Optional.of(forex);
        when(forexRepository.findById(VALID_ID)).thenReturn(optional);
        ForexDTO actual = underTest.findById(VALID_ID);
        assertEquals(new ForexDTO(forex), actual);
        assertNotNull(actual);
    }

    @Test
    public void findById_ReturnsNULL_ForInvalidID() {
        Optional<Forex> optional = Optional.empty();
        when(forexRepository.findById(INVALID_ID)).thenReturn(optional);
        ForexDTO actual = underTest.findById(INVALID_ID);
        assertNull(actual);
    }
}
