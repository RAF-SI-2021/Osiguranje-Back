package app.controllers;

import app.model.dto.*;
import app.services.ForexService;
import app.services.FuturesService;
import app.services.OptionsService;
import app.services.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static app.controllers.ModelType.*;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith( MockitoExtension.class )
class ControllerTest {

    @Mock
    private ForexService forexService;
    @Mock
    private FuturesService futuresService;
    @Mock
    private StockService stockService;
    @Mock
    private OptionsService optionsService;

    private Controller underTest;

    @BeforeEach
    void setUp() {
        underTest = new Controller( futuresService, forexService, stockService, optionsService );
    }


    @Test
    void getSecurities_NoResults() throws Exception {
        /* Given. */
        List<ForexDTO> forexDTOS = new ArrayList<>();
        List<FutureDTO> futureDTOS = new ArrayList<>();
        List<StockDTO> stockDTOS = new ArrayList<>();
        List<OptionDTO> optionDTOS = new ArrayList<>();

        lenient().when( forexService.getForexDTOData() ).thenReturn( forexDTOS );
        lenient().when( futuresService.getFutureDTOData() ).thenReturn( futureDTOS );
        lenient().when( stockService.getStocksDTOData() ).thenReturn( stockDTOS );
        lenient().when( optionsService.getOptionsDTOData() ).thenReturn( optionDTOS );

        /* When. */
        ResponseEntity<?> responseEmpty = underTest.getSecurities();

        /* Then. */
        assertEquals( ( ( DataDTO ) responseEmpty.getBody() ).getForex().size(), 0 );
        assertEquals( ( ( DataDTO ) responseEmpty.getBody() ).getFutures().size(), 0 );
        assertEquals( ( ( DataDTO ) responseEmpty.getBody() ).getStocks().size(), 0 );
    }

    @Test
    void getSecurities_WithResults() throws Exception {
        /* Given. */
        List<ForexDTO> forexDTOS = new ArrayList<>();
        List<FutureDTO> futureDTOS = new ArrayList<>();
        List<StockDTO> stockDTOS = new ArrayList<>();
        List<OptionDTO> optionDTOS = new ArrayList<>();

        forexDTOS.add( ( ForexDTO ) DtoFactory.generatePlainDTO( FOREX ) );
        futureDTOS.add( ( FutureDTO ) DtoFactory.generatePlainDTO( FUTURE ) );
        stockDTOS.add( ( StockDTO ) DtoFactory.generatePlainDTO( STOCK ) );
        optionDTOS.add( ( OptionDTO ) DtoFactory.generatePlainDTO( OPTION ) );

        lenient().when( forexService.getForexDTOData() ).thenReturn( forexDTOS );
        lenient().when( futuresService.getFutureDTOData() ).thenReturn( futureDTOS );
        lenient().when( stockService.getStocksDTOData() ).thenReturn( stockDTOS );
        lenient().when( optionsService.getOptionsDTOData() ).thenReturn( optionDTOS );

        /* When. */
        ResponseEntity<?> responseNotEmpty = underTest.getSecurities();

        /* Then. */
        assertEquals( ( ( DataDTO ) responseNotEmpty.getBody() ).getForex().size(), 1 );
        assertEquals( ( ( DataDTO ) responseNotEmpty.getBody() ).getFutures().size(), 1 );
        assertEquals( ( ( DataDTO ) responseNotEmpty.getBody() ).getStocks().size(), 1 );
    }


    @Test
    void getOptions() throws Exception {
        /* Given. */
        List<OptionDTO> optionDTOS = new ArrayList<>();

        when( optionsService.getOptionsDTOData() ).thenReturn( optionDTOS );

        /* When. */
        ResponseEntity<?> response1 = underTest.getOptions();

        optionDTOS.add( ( OptionDTO ) DtoFactory.generatePlainDTO( OPTION ) );
        ResponseEntity<?> response2 = underTest.getOptions();

        /* Then. */
        assertEquals( response1.getBody(), optionDTOS );
        assertEquals( response2.getBody(), optionDTOS );
    }

    @Test
    void findForexById_WhenExists() throws Exception {
        /* Given. */
        ForexDTO forex = ( ForexDTO ) DtoFactory.generatePlainDTO( FOREX );
        final long ID = 123;
        forex.setId( ID );
        when( forexService.findById( ID ) ).thenReturn( forex );

        /* When. */
        ResponseEntity<?> response1 = underTest.findForexById( ID );

        /* Then. */
        assertEquals( response1.getBody(), forex );
    }

    @Test
    void findForexById_NotFound() {
        /* Given. */
        final long ID = 123;
        when( forexService.findById( ID ) ).thenReturn( null );

        /* When. */
        ResponseEntity<?> responseNotFound = underTest.findForexById( ID );

        /* Then. */
        assertEquals( responseNotFound.getStatusCode(), HttpStatus.NOT_FOUND );
    }

    @Test
    void findFutureById_WhenExists() throws Exception {
        /* Given. */
        FutureDTO future = ( FutureDTO ) DtoFactory.generatePlainDTO( FUTURE );
        final long ID = 123;
        future.setId( ID );
        when( futuresService.findById( ID ) ).thenReturn( future );

        /* When. */
        ResponseEntity<?> response1 = underTest.findFutureById( ID );


        /* Then. */
        assertEquals( response1.getBody(), future );
    }

    @Test
    void findFutureById_NotFound() {
        /* Given. */
        final long ID = 123;
        when( futuresService.findById( ID ) ).thenReturn( null );

        /* When. */
        ResponseEntity<?> responseNotFound = underTest.findFutureById( ID );

        /* Then. */
        assertEquals( responseNotFound.getStatusCode(), HttpStatus.NOT_FOUND );
    }

    @Test
    void findStockById_WhenExists() throws Exception {
        /* Given. */
        StockDTO stock = ( StockDTO ) DtoFactory.generatePlainDTO( STOCK );
        final long ID = 123;
        stock.setId( ID );
        when( stockService.findById( ID ) ).thenReturn( stock );

        /* When. */
        ResponseEntity<?> response1 = underTest.findStockById( ID );

        /* Then. */
        assertEquals( response1.getBody(), stock );
    }

    @Test
    void findStockById_NotFound() {
        /* Given. */
        final long ID = 123;
        when( stockService.findById( ID ) ).thenReturn( null );

        /* When. */

        ResponseEntity<?> responseNotFound = underTest.findStockById( ID );

        /* Then. */
        assertEquals( responseNotFound.getStatusCode(), HttpStatus.NOT_FOUND );
    }
}