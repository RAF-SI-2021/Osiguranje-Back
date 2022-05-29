package security.test;

import app.model.Currency;
import app.model.Exchange;
import app.model.Region;
import app.model.Stock;
import app.model.dto.StockDTO;
import app.repositories.StocksRepository;
import app.services.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StockServiceTest {
    private StocksRepository stocksRepository = mock(StocksRepository.class);
    private static final long VALID_ID = 1;
    private static final long INVALID_ID = 2;
    private static final String symbol = "SYMBOL";
    private static final String description = "DESCRIPTION";
    private static final Exchange exchange = mock(Exchange.class);
    private static final Region exchangeRegion = mock(Region.class);
    private static final Region currencyRegion = mock(Region.class);
    private static final Currency exchangeCurrency = mock(Currency.class);
    private static final String lastUpdated = "LAST_UPDATED";
    private static final BigDecimal price = new BigDecimal("1.0");
    private static final BigDecimal ask = new BigDecimal("1.0");
    private static final BigDecimal bid = new BigDecimal("1.0");
    private static final BigDecimal priceChange = new BigDecimal("1.0");
    private static final Long volume = 1L;
    private static final Long outstandingShares = 1L;
    private static final BigDecimal dividendYield = new BigDecimal("1.0");

    private StockService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StockService(stocksRepository);
        exchange.setRegion(exchangeRegion);
        exchange.setCurrency(exchangeCurrency);
        when(exchange.getRegion()).thenReturn(exchangeRegion);
        when(exchange.getCurrency()).thenReturn(exchangeCurrency);
        when(exchangeCurrency.getRegion()).thenReturn(currencyRegion);
    }

    @Test
    public void findById_ReturnsStockDTO_ForValidID() {
        Stock stock = new Stock(symbol, description, exchange, lastUpdated,
                price, ask, bid, priceChange, volume,
                outstandingShares, dividendYield);
        Optional<Stock> optional = Optional.of(stock);
        when(stocksRepository.findById(VALID_ID)).thenReturn(optional);
        StockDTO actual = underTest.findById(VALID_ID);
        assertEquals(new StockDTO(stock), actual);
        assertNotNull(actual);
    }

    @Test
    public void findById_ReturnsNULL_ForInvalidID() {
        Optional<Stock> optional = Optional.empty();
        when(stocksRepository.findById(INVALID_ID)).thenReturn(optional);
        StockDTO actual = underTest.findById(INVALID_ID);
        assertNull(actual);
    }

    @Test
    public void save_ReturnsStock_and_CallsStocksRepositorySave_Once() {
        Stock stock = new Stock(symbol, description, exchange, lastUpdated,
                price, ask, bid, priceChange, volume,
                outstandingShares, dividendYield);
        when(stocksRepository.save(any())).thenReturn(stock);
        Stock actual = underTest.save(stock);
        assertEquals(stock, actual);
        verify(stocksRepository, times(1)).save(stock);
    }

    @Test
    public void getStocksDTOData_ReturnsStockDTOList() {
        Stock stock = new Stock(symbol, description, exchange, lastUpdated,
                price, ask, bid, priceChange, volume,
                outstandingShares, dividendYield);
        List<Stock> stockList = new ArrayList<>();
        stockList.add(stock);
        List<StockDTO> stockDTOList = new ArrayList<>();
        stockDTOList.add(new StockDTO(stock));

        when(stocksRepository.findAll()).thenReturn(stockList);
        List<StockDTO> actual = underTest.getStocksDTOData();
        assertEquals(stockDTOList, actual);
        verify(stocksRepository, times(1)).findAll();
    }

    @Test
    public void getStocksData_ReturnsStockList() {
        Stock stock = new Stock(symbol, description, exchange, lastUpdated,
                price, ask, bid, priceChange, volume,
                outstandingShares, dividendYield);
        List<Stock> stockList = new ArrayList<>();
        stockList.add(stock);

        when(stocksRepository.findAll()).thenReturn(stockList);
        List<Stock> actual = underTest.getStocksData();
        assertEquals(stockList, actual);
        verify(stocksRepository, times(1)).findAll();
    }

    @Test
    public void updateData_DoesNothing_ForInvalidStockList() {
        Stock stock = new Stock(symbol, description, exchange, lastUpdated,
                price, ask, bid, priceChange, volume,
                outstandingShares, dividendYield);
        List<Stock> stockList = new ArrayList<>();
        stockList.add(stock);

        when(stocksRepository.findAll()).thenReturn(stockList);
        try {
            underTest.updateData();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        verify(stocksRepository, times(0)).save(any());
    }

    @Test
    public void updateData_CallsStocksRepositorySave() {
        Stock stock1 = new Stock("MSFT", description, exchange, lastUpdated,
                price, ask, bid, priceChange, volume,
                outstandingShares, dividendYield);
        List<Stock> stockList = new ArrayList<>();
        stockList.add(stock1);

        when(stocksRepository.findAll()).thenReturn(stockList);
        try {
            underTest.updateData();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        int n = stockList.size();
        verify(stocksRepository, times(n)).save(any());
    }
}
