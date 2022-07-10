package app.integration;

import app.model.Company;
import app.model.Contract;
import app.model.Status;
import app.model.dto.ContractDTO;
import app.model.dto.CreateContractDTO;
import app.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@ActiveProfiles( value = "test" )
@TestPropertySource( locations = "classpath:application.properties" )
@AutoConfigureMockMvc // ( addFilters = false )
// @DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_CLASS )
public class ContractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private TransactionItemRepository transactionItemRepository;

    @Autowired
    private ObjectMapper mapper;

    private final String BASE_URL = "/api/contracts/";

    @Test
    @Transactional
    void createAndReadContracts() throws Exception {
        Company company = new Company();
        company.setEmployees( new ArrayList<>() );
        company.setBankAccounts( new ArrayList<>() );
        company.setContracts( new ArrayList<>() );
        company = companyRepository.save( company );

        /* Create. */
        CreateContractDTO createContractDTO = mapper.readValue(
                "{ \"companyID\": \"-1\", \"refNumber\": \"-1\", \"description\": \"\" }", CreateContractDTO.class
        );
        mockMvc.perform( post( BASE_URL )
                .header( "Authorization", "test" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( createContractDTO ) ) )
                .andExpect( status().isBadRequest() );

        final int CONTRACT_NUMBER = 10;
        for( long i = 0; i < CONTRACT_NUMBER; i++ )
        {
            String createContractDtoJson = String.format(
                    "{ \"companyID\": \"%d\", \"refNumber\": \"%d\", \"description\": \"\" }", company.getId(), i
            );
            createContractDTO = mapper.readValue( createContractDtoJson, CreateContractDTO.class );

            mockMvc.perform( post( BASE_URL )
                    .header( "Authorization", "test" )
                    .contentType( MediaType.APPLICATION_JSON )
                    .content( asJsonString( createContractDTO ) ) )
                    .andExpect( status().isOk() );
        }

        Optional<Company> optionalCompany = companyRepository.findById( company.getId() );
        assertTrue( optionalCompany.isPresent() );

        company = optionalCompany.get();
        assertEquals( CONTRACT_NUMBER, company.getContracts().size() );

        /* Read. */
        for( Contract c: company.getContracts() )
        {
            /* Otherwise it produces null pointer in the controller if we request contract that has not transactions. */
            c.setTransactionItems( new ArrayList<>() );
            c = contractRepository.save( c );

            MvcResult result = mockMvc.perform( get( BASE_URL + c.getId() ).header( "Authorization", "test" ) )
                    .andExpect( status().isOk() )
                    .andReturn();

            assertEquals( asJsonString( new ContractDTO( c ) ), result.getResponse().getContentAsString() );
        }

        /* Get all contracts. */
        MvcResult result = mockMvc.perform( get( BASE_URL ).header( "Authorization", "test" ) )
                .andExpect( status().isOk() )
                .andReturn();

        List<?> contracts = mapper.readValue( result.getResponse().getContentAsString(), List.class );
        assertEquals( company.getContracts().size(), contracts.size() );

    }

    private String asJsonString( final Object obj ) {
        try {
            return new ObjectMapper().writeValueAsString( obj );
        } catch( Exception e ) {
            throw new RuntimeException( e );
        }
    }

}
