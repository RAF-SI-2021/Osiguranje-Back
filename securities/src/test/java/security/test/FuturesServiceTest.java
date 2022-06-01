package security.test;

import app.model.Future;
import app.model.dto.FutureDTO;
import app.repositories.FuturesRepository;
import app.services.FuturesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FuturesServiceTest {
    private FuturesRepository futuresRepository = mock(FuturesRepository.class);
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
    private static final String contractUnit = "CONTRACT_UNIT";
    private static final Integer maintenanceMargin = 1;
    private static final Date settlementDate = new Date();
    private FuturesService underTest;

    @BeforeEach
    void setUp() {
        underTest = new FuturesService(futuresRepository);
    }

    @Test
    public void findById_ReturnsFutureDTO_ForValidID() {
        Future future = new Future(symbol, description, lastUpdated,
                                    price, ask, bid, priceChange, volume, contractSize,
                                    contractUnit, maintenanceMargin, settlementDate);
        Optional<Future> optional = Optional.of(future);
        when(futuresRepository.findById(VALID_ID)).thenReturn(optional);
        FutureDTO actual = underTest.findById(VALID_ID);
        assertEquals(new FutureDTO(future), actual);
        assertNotNull(actual);
    }

    @Test
    public void findById_ReturnsNULL_ForInvalidID() {
        Optional<Future> optional = Optional.empty();
        when(futuresRepository.findById(INVALID_ID)).thenReturn(optional);
        FutureDTO actual = underTest.findById(INVALID_ID);
        assertNull(actual);
    }

    @Test
    public void getFuturesData_callsFuturesRepositoryFindAll_Once() {
        List<Future> actual = underTest.getFuturesData();
        verify(futuresRepository, times(1)).findAll();
    }

    @Test
    public void getFuturesDTOData_ReturnsFuturesDTOList() {
        Future future = new Future(symbol, description, lastUpdated,
                                    price, ask, bid, priceChange, volume, contractSize,
                                    contractUnit, maintenanceMargin, settlementDate);
        List<Future> futureList = new ArrayList<>();
        futureList.add(future);
        List<FutureDTO> futureDTOList = new ArrayList<>();
        futureDTOList.add(new FutureDTO(future));
        when(futuresRepository.findAll()).thenReturn(futureList);
        List<FutureDTO> actual = underTest.getFutureDTOData();
        assertEquals(futureDTOList, actual);
        verify(futuresRepository, times(1)).findAll();
    }

    @Test
    public void save_ReturnsFuture_and_CallsFuturesRepositorySave_Once() {
        Future future = new Future(symbol, description, lastUpdated,
                                    price, ask, bid, priceChange, volume, contractSize,
                                    contractUnit, maintenanceMargin, settlementDate);
        when(futuresRepository.save(any())).thenReturn(future);
        Future actual = underTest.save(future);
        assertEquals(future, actual);
        verify(futuresRepository, times(1)).save(future);
    }
}
