package app.integration;


import app.model.*;
import app.model.dto.DataDTO;
import app.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@ActiveProfiles( value = "test" )
@TestPropertySource( locations = "classpath:application.properties" )
@AutoConfigureMockMvc
@DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_CLASS )
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ForexRepository forexRepository;
    @Autowired
    private StocksRepository stocksRepository;
    @Autowired
    private FuturesRepository futuresRepository;
    @Autowired
    private OptionsRepository optionsRepository;

    @Autowired
    private ObjectMapper mapper;

    private final String BASE_URL = "/api/data/";

    @Test
    void forexTest() throws Exception {
        /* Given. */
        Currency currency = new Currency();
        currencyRepository.save( currency );

        Forex forex = new Forex();
        forex.setBaseCurrency( currency );
        forex.setQuoteCurrency( currency );

        forexRepository.save( forex );
        System.out.println( forexRepository.findAll() );
        final long ID = forex.getId();

        /* When and then. */
        mockMvc.perform( get( BASE_URL + "forex/" + ID ) ).andExpect( status().isOk() );

        /* This request should always fail because IDs are positive integers only. */
        mockMvc.perform( get( BASE_URL + "forex/-1" ) ).andExpect( status().isNotFound() );
    }

    @Test
    void stockTest() throws Exception {
        /* Given. */
        Stock stock = new Stock();
        stocksRepository.save( stock );

        System.out.println( stocksRepository.findAll() );
        final long ID = stock.getId();

        /* When and then. */
        mockMvc.perform( get( BASE_URL + "stocks/" + ID ) ).andExpect( status().isOk() );

        /* This request should always fail because IDs are positive integers only. */
        mockMvc.perform( get( BASE_URL + "stocks/-1" ) ).andExpect( status().isNotFound() );
    }

    @Test
    void futureTest() throws Exception {
        /* Given. */
        Future future = new Future();
        future.setSettlementDate( new Date() );
        future.setMaintenanceMargin( new BigDecimal( Math.PI ) );
        future.setContractUnit( "test" );
        futuresRepository.save( future );

        System.out.println( futuresRepository.findAll() );
        final long ID = future.getId();

        /* When and then. */
        mockMvc.perform( get( BASE_URL + "futures/" + ID ) ).andExpect( status().isOk() );

        /* This request should always fail because IDs are positive integers only. */
        mockMvc.perform( get( BASE_URL + "futures/-1" ) ).andExpect( status().isNotFound() );
    }


    @Test
    void optionsTest() throws Exception {
        /* Given. */
        final List<StockOption> options = optionsRepository.findAll();

        /* When. */
        String jsonResponse
                = mockMvc.perform( get( BASE_URL + "options" ) )
                .andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        List responseDTOs = mapper.readValue( jsonResponse, List.class );

        /* Then. */
        System.out.println( responseDTOs.size() + " ?= " + options.size() );
        assertThat( responseDTOs.size() == options.size() );

    }

    @Test
    void applicationTest() throws Exception {
        /* Given. */
        final int forexReturns = forexRepository.findAll().size();
        final int futureReturns = futuresRepository.findAll().size();
        final int stockReturns = stocksRepository.findAll().size();

        /* When. */
        String jsonResponse
                = mockMvc.perform( get( BASE_URL ) )
                .andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        /* A bit hacky but it'll work. */
        final int responseReturns = countSubstring( jsonResponse, "\"id\"" );

        // DataDTO dataDTO = mapper.readValue( jsonResponse, DataDTO.class );
        // final int responseReturns = dataDTO.getForex().size() + dataDTO.getFutures().size() + dataDTO.getStocks().size();

        /* Then. */
        assertThat( forexReturns + futureReturns + stockReturns == responseReturns );
    }

    public static int countSubstring( String text, String find ) {
        int index = 0, count = 0, length = find.length();
        while( ( index = text.indexOf( find, index ) ) != -1 ) {
            index += length; count++;
        }
        return count;
    }

}
