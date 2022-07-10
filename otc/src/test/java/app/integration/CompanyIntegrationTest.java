package app.integration;

import app.model.BankAccount;
import app.model.Company;
import app.model.Employee;
import app.model.dto.BankAccountDTO;
import app.model.dto.CompanyDTO;
import app.model.dto.EmployeeDTO;
import app.repositories.BankAccountRepository;
import app.repositories.CompanyRepository;
import app.repositories.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@ActiveProfiles( value = "test" )
@TestPropertySource( locations = "classpath:application.properties" )
@AutoConfigureMockMvc
// @DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_CLASS )
public class CompanyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private ObjectMapper mapper;

    private final String BASE_URL = "/api/companies/";

    @Test
    void companyCrudTest() throws Exception {
        /* Create. */
        String name = "TEST_NAME";
        CompanyDTO dtoToSave = new CompanyDTO( 0L, 0L, name, 0L,
                0L, "TEST_ADDRESS", new ArrayList<>(), new ArrayList<>()
        );

        mockMvc.perform( post( BASE_URL )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( dtoToSave ) ) )
                .andExpect( status().isOk() );

        /* Read. */
        Company saved = companyRepository.findAllByName( name ).get( 0 );

        mockMvc.perform( get( BASE_URL + saved.getId() ) ).andExpect( status().isOk() );
        mockMvc.perform( get( BASE_URL + "-1" ) ).andExpect( status().isBadRequest() );

        /* Update. */
        name = "UPDATED";
        dtoToSave.setId( saved.getId() );
        dtoToSave.setName( name );
        mockMvc.perform( put( BASE_URL )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( dtoToSave ) ) )
                .andExpect( status().isOk() );

        Optional<Company> optionalUpdated = companyRepository.findById( saved.getId() );
        assertTrue( optionalUpdated.isPresent() );

        Company updated = optionalUpdated.get();
        assertEquals( name, updated.getName() );

        /* Delete. */
        mockMvc.perform( delete( BASE_URL + "-1" ) ).andExpect( status().isBadRequest() );
        mockMvc.perform( delete( BASE_URL + updated.getId() ) ).andExpect( status().isOk() );

        Optional<Company> optionalDeleted = companyRepository.findById( updated.getId() );
        assertFalse( optionalDeleted.isPresent() );

    }

    @Test
    @Transactional
    void employeeCrudTest() throws Exception {
        /* Given. */
        Company company = companyRepository.save(
                new Company( 0L, "TEST_NAME", 0L, 0L, "TEST_ADDRESS" )
        );

        /* Create. */
        String name = "TEST_EMPLOYEE_NAME";
        EmployeeDTO dtoToSave = new EmployeeDTO( 0L, name, "", "", "", "", "" );

        mockMvc.perform( post( BASE_URL + "-1" + "/employees" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( dtoToSave ) ) )
                .andExpect( status().isBadRequest() );

        mockMvc.perform( post( BASE_URL + company.getId() + "/employees" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( dtoToSave ) ) )
                .andExpect( status().isOk() );

        company = companyRepository.findById( company.getId() ).get();
        Optional<Employee> optionalEmployee = employeeRepository.findById( company.getEmployees().get( 0 ).getId() );
        assertTrue( optionalEmployee.isPresent() );

        Employee saved = optionalEmployee.get();
        assertEquals( name, saved.getName() );

        /* Read. Nothing to do... */

        /* Update. */
        name = "UPDATED";
        dtoToSave.setId( saved.getId() );
        dtoToSave.setName( name );

        mockMvc.perform( put( BASE_URL + "-1" + "/employees" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( dtoToSave ) ) )
                .andExpect( status().isBadRequest() );

        mockMvc.perform( put( BASE_URL + company.getId() + "/employees" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( dtoToSave ) ) )
                .andExpect( status().isOk() );

        Optional<Employee> optionalUpdated = employeeRepository.findById( saved.getId() );
        assertTrue( optionalUpdated.isPresent() );

        Employee updated = optionalUpdated.get();
        assertEquals( name, updated.getName() );

        /* Delete. */
        mockMvc.perform( delete( BASE_URL + company.getId() + "/employees/" + updated.getId() ) )
                .andExpect( status().isOk() );

        Optional<Employee> deletedOptional = employeeRepository.findById( updated.getId() );
        assertFalse( deletedOptional.isPresent() );

    }

    @Test
    @Transactional
    void bankAccountCrudTest() throws Exception {
        /* Given. */
        Company company = companyRepository.save(
                new Company( 0L, "TEST_NAME", 0L, 0L, "TEST_ADDRESS" )
        );

        /* Create. */
        String name = "TEST_BANK_NAME";
        BankAccountDTO dtoToSave = new BankAccountDTO( 0L, "", name, "" );

        mockMvc.perform( post( BASE_URL + "-1" + "/bank-accounts" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( dtoToSave ) ) )
                .andExpect( status().isBadRequest() );

        mockMvc.perform( post( BASE_URL + company.getId() + "/bank-accounts" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( dtoToSave ) ) )
                .andExpect( status().isOk() );

        company = companyRepository.findById( company.getId() ).get();
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById( company.getBankAccounts().get( 0 ).getId() );
        assertTrue( optionalBankAccount.isPresent() );

        BankAccount saved = optionalBankAccount.get();
        assertEquals( name, saved.getBankName() );

        /* Read. Nothing to do... */

        /* Update. */
        name = "UPDATED";
        dtoToSave.setId( saved.getId() );
        dtoToSave.setBankName( name );

        mockMvc.perform( put( BASE_URL + "-1" + "/bank-accounts" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( dtoToSave ) ) )
                .andExpect( status().isBadRequest() );

        mockMvc.perform( put( BASE_URL + company.getId() + "/bank-accounts" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( dtoToSave ) ) )
                .andExpect( status().isOk() );

        Optional<BankAccount> optionalUpdated = bankAccountRepository.findById( saved.getId() );
        assertTrue( optionalUpdated.isPresent() );

        BankAccount updated = optionalUpdated.get();
        assertEquals( name, updated.getBankName() );

        /* Delete. */
        mockMvc.perform( delete( BASE_URL + company.getId() + "/bank-accounts/" + updated.getId() ) )
                .andExpect( status().isOk() );

        Optional<Employee> deletedOptional = employeeRepository.findById( updated.getId() );
        assertFalse( deletedOptional.isPresent() );

    }

    private String asJsonString( final Object obj ) {
        try {
            return new ObjectMapper().writeValueAsString( obj );
        } catch( Exception e ) {
            throw new RuntimeException( e );
        }
    }

}
