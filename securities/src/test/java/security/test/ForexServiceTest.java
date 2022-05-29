package security.test;

import app.model.Currency;
import app.model.Forex;
import app.model.Region;
import app.model.api.ExchangeRateAPIResponse;
import app.model.dto.ForexDTO;
import app.repositories.CurrencyRepository;
import app.repositories.ForexRepository;
import app.services.ForexService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ForexServiceTest {
    private ForexRepository forexRepository = mock(ForexRepository.class);
    private CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
    private RestTemplate restTemplate = mock(RestTemplate.class);
    private ResponseEntity response = mock(ResponseEntity.class);
    private ExchangeRateAPIResponse responseBody = mock(ExchangeRateAPIResponse.class);
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

    @Test
    public void getForexDTOData_ReturnsForexDTOList() {
        Forex forex = new Forex(symbol, description, lastUpdated,
                                price, ask, bid, priceChange, volume,
                                baseCurrency, quoteCurrency, contractSize);
        List<Forex> forexList = new ArrayList<>();
        forexList.add(forex);
        List<ForexDTO> forexDTOList = new ArrayList<>();
        forexDTOList.add(new ForexDTO(forex));
        when(forexRepository.findAll()).thenReturn(forexList);
        List<ForexDTO> actual = underTest.getForexDTOData();
        assertEquals(forexDTOList, actual);
        verify(forexRepository, times(1)).findAll();
    }

    @Test
    public void updateData_callsCurrencyRepositoryFindAll_Once() {
        underTest.updateData();
        verify(currencyRepository, times(1)).findAll();
    }

    @Test
    public void updateData_callsForexRepositoryMethods_WhenCurrencyList_NotEmpty() {
        Forex forex = new Forex(symbol, description, lastUpdated,
                price, ask, bid, priceChange, volume,
                baseCurrency, quoteCurrency, contractSize);
        Currency currency1 = new Currency("name", "EUR", "symbol", new Region("name", "code"));
        Currency currency2 = new Currency("name", "USD", "symbol", new Region("name", "code"));
        List<Currency> currencies = new ArrayList<>();
        currencies.add(currency1);
        currencies.add(currency2);

        when(currencyRepository.findAll()).thenReturn(currencies);
        when(forexRepository.findForexByTicker(any())).thenReturn(forex);
        underTest.updateData();

        int n = currencies.size();
        verify(forexRepository, times(n * (n - 1))).findForexByTicker(any());
        verify(forexRepository, times(n * (n - 1))).save(any());
    }

    @Test
    public void save_CallsForexRepositorySave_Once() {
        Forex forex = new Forex(symbol, description, lastUpdated,
                price, ask, bid, priceChange, volume,
                baseCurrency, quoteCurrency, contractSize);
        underTest.save(forex);
        verify(forexRepository).save(forex);
    }
}
