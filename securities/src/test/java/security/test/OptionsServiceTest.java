package security.test;

import app.model.*;
import app.model.dto.OptionDTO;
import app.repositories.OptionsRepository;
import app.services.OptionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OptionsServiceTest {
    private OptionsRepository optionsRepository = mock(OptionsRepository.class);
    private static final String ticker = "TICKER";
    private static final String name = "NAME";
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
    private static int contractSize = 1;
    private OptionsService underTest;

    @BeforeEach
    void setUp() {
        underTest = new OptionsService(optionsRepository);
        exchange.setRegion(exchangeRegion);
        exchange.setCurrency(exchangeCurrency);
        when(exchange.getRegion()).thenReturn(exchangeRegion);
        when(exchange.getCurrency()).thenReturn(exchangeCurrency);
        when(exchangeCurrency.getRegion()).thenReturn(currencyRegion);
    }

    @Test
    public void save_CallsOptionsRepositorySave_Once() {
        StockOption option = new StockOption(ticker, name, exchange,
                                    lastUpdated, price, ask,
                                    bid, priceChange, volume, contractSize);
        when(optionsRepository.save(any())).thenReturn(option);
        StockOption actual = underTest.save(option);
        assertEquals(option, actual);
        verify(optionsRepository, times(1)).save(option);
    }

    @Test
    public void getOptionsDTOData_ReturnsOptionsDTOList() {
        StockOption option = new StockOption(ticker, name, exchange,
                lastUpdated, price, ask,
                bid, priceChange, volume, contractSize);
        List<StockOption> optionList = new ArrayList<>();
        optionList.add(option);
        List<OptionDTO> optionDTOList = new ArrayList<>();
        optionDTOList.add(new OptionDTO(option));
        when(optionsRepository.findAll()).thenReturn(optionList);
        List<OptionDTO> actual = underTest.getOptionsDTOData();
        assertEquals(optionDTOList, actual);
        verify(optionsRepository, times(1)).findAll();
    }

    @Test
    public void getOptionsData_ReturnsOptionList() {
        StockOption option = new StockOption(ticker, name, exchange,
                lastUpdated, price, ask,
                bid, priceChange, volume, contractSize);
        List<StockOption> optionList = new ArrayList<>();
        optionList.add(option);

        when(optionsRepository.findAll()).thenReturn(optionList);
        List<StockOption> actual = underTest.getOptionsData();
        assertEquals(optionList, actual);
        verify(optionsRepository, times(1)).findAll();
    }

    @Test
    public void updateData_CallsOptionsRepositorySave_ForEveryOption() {
        StockOption option = new StockOption("MSFT", name, exchange,
                lastUpdated, price, ask,
                bid, priceChange, volume, contractSize);
        option.setOptionType(OptionType.PUT);
        List<StockOption> optionList = new ArrayList<>();
        optionList.add(option);

        when(optionsRepository.findAll()).thenReturn(optionList);
        underTest.updateData();

        int n = optionList.size();
        verify(optionsRepository, times(1)).findAll();
        verify(optionsRepository, times(n)).save(any());
    }
}
