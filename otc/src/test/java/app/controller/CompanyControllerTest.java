package app.controller;

import app.model.BankAccount;
import app.model.Company;
import app.model.Contract;
import app.model.Employee;
import app.model.dto.BankAccountDTO;
import app.model.dto.CompanyDTO;
import app.model.dto.EmployeeDTO;
import app.services.BankAccountService;
import app.services.CompanyService;
import app.services.EmployeeService;
import org.hibernate.engine.jdbc.batch.spi.BatchKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class CompanyControllerTest {

    @Mock
    private CompanyService companyService;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private BankAccountService bankAccountService;

    private CompanyController underTest;

    @BeforeEach
    void setUp() {
        underTest = new CompanyController( companyService, employeeService, bankAccountService );
    }

    private List<Company> generateDummyCompanies( String name, long taxID, long regID ) {
        List<Company> companies = new ArrayList<>();

        for( long i = 0; i < 10; i++ )
        {
            Company company = new Company();
            company.setId( i );
            company.setTaxID( taxID );
            company.setRegistrationID( regID );
            company.setName( name );
            company.setEmployees( new ArrayList<>() );
            company.setBankAccounts( new ArrayList<>() );

            companies.add( company );
        }

        return companies;
    }

    @Test
    void getCompanies() {
        /* Given. */
        final String NAME = "TEST_NAME";
        final long TAX_ID = 0, REG_ID = 0;

        List<Company> companies = generateDummyCompanies( NAME, TAX_ID, REG_ID );

        when( companyService.findByName( NAME ) ).thenReturn( companies );
        when( companyService.findByTaxID( TAX_ID ) ).thenReturn( companies );
        when( companyService.findByRegistrationID( REG_ID ) ).thenReturn( companies );

        /* When. */
        ResponseEntity<?> nameResponse = underTest.getCompanies( NAME, null, null );
        ResponseEntity<?> taxIdResponse = underTest.getCompanies( null, TAX_ID, null );
        ResponseEntity<?> regIdResponse = underTest.getCompanies( null, null, REG_ID );

        List<CompanyDTO> nameResults = ( List<CompanyDTO> ) nameResponse.getBody();
        List<CompanyDTO> taxIdResults = ( List<CompanyDTO> ) taxIdResponse.getBody();
        List<CompanyDTO> regIdResults = ( List<CompanyDTO> ) regIdResponse.getBody();

        /* Then. */
        int i = 0;
        for( Company c: companies )
        {
            assertEquals( nameResults.get( i ).getName(), c.getName() );
            assertEquals( taxIdResults.get( i ).getTaxID(), c.getTaxID() );
            assertEquals( regIdResults.get( i++ ).getRegistrationID(), c.getRegistrationID() );
        }
    }

    @Test
    void getCompanyByID_WhenExists() {
        /* Given. */
        final long ID = 123;
        Company company = new Company();
        company.setId( ID );
        company.setEmployees( new ArrayList<>() );
        company.setBankAccounts( new ArrayList<>() );

        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );

        /* When. */
        ResponseEntity<?> result = underTest.getCompanyByID( ID );

        /* Then. */
        assertEquals( ( ( CompanyDTO ) result.getBody() ).getId(), company.getId() );
    }

    @Test
    void getCompanyByID_NotFound() {
        /* Given. */
        final long ID = 123;
        when( companyService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> result = underTest.getCompanyByID( ID );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void createCompany() {
        /* Given. */
        CompanyDTO dto = new CompanyDTO();

        /* When. */
        ResponseEntity<?> result = underTest.createCompany( dto );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void updateCompany() {
        /* Given. */
        CompanyDTO dto = new CompanyDTO();
        when( companyService.update( dto ) ).thenReturn( dto );

        /* When. */
        ResponseEntity<?> result = underTest.updateCompany( dto );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void deleteCompany_WhenNotFound() {
        /* Given. */
        final long ID = 123;
        when( companyService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> result = underTest.deleteCompany( ID );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void deleteCompany_WithContracts() {
        /* Given. */
        final long ID = 123;
        Company company = new Company();
        company.setId( ID );
        company.setContracts (Arrays.asList( new Contract() ) ); /* Deleting company with contracts is not allowed. */

        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );

        /* When. */
        ResponseEntity<?> result = underTest.deleteCompany( ID );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void deleteCompany() {
        /* Given. */
        final long ID = 123;
        Company company = new Company();
        company.setId( ID );
        company.setContracts ( new ArrayList<>() ); /* Deleting company with contracts is not allowed. */

        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );

        /* When. */
        ResponseEntity<?> result = underTest.deleteCompany( ID );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void createEmployee_WhenCompanyNotFound() {
        /* Given. */
        final long ID = 123;
        when( companyService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> result = underTest.createEmployee( ID, new EmployeeDTO());

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void createEmployee() {
        /* Given. */
        final long ID = 123;
        Company company = new Company();
        company.setEmployees( new ArrayList<>() );
        company.setId( ID );

        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );

        EmployeeDTO dto = new EmployeeDTO();

        /* When. */
        ResponseEntity<?> result = underTest.createEmployee( ID, dto );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.OK );
    }


    @Test
    void createBankAccount_WhenCompanyNotFound() {
        /* Given. */
        final long ID = 123;
        when( companyService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> result = underTest.createBankAccount(
                ID, new BankAccountDTO( null, null, null, null )
        );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void createBankAccount() {
        /* Given. */
        final long ID = 123;
        Company company = new Company();
        company.setBankAccounts( new ArrayList<>() );
        company.setId( ID );

        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );

        BankAccountDTO dto = new BankAccountDTO( null, null, null, null );

        /* When. */
        ResponseEntity<?> result = underTest.createBankAccount( ID, dto );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void updateEmployee_WhenCompanyNotFound() {
        /* Given. */
        final long ID = 123;
        when( companyService.findByID( ID ) ).thenReturn( Optional.empty() );

        /* When. */
        ResponseEntity<?> result = underTest.updateEmployee(
                ID, new EmployeeDTO()
        );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void updateEmployee_WhenEmployeeNotFound() {
        /* Given. */
        final long ID = 123;
        Company company = new Company();
        company.setId( ID );

        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );
        when( employeeService.findByID( ID ) ).thenReturn( Optional.empty() );

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId( ID );

        /* When. */
        ResponseEntity<?> result = underTest.updateEmployee( ID, dto );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void updateEmployee() {
        /* Given. */
        final long ID = 123;
        Company company = new Company();
        company.setId( ID );

        Employee employee = new Employee();
        employee.setId( ID );

        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );
        when( employeeService.findByID( ID ) ).thenReturn( Optional.of( employee ) );

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId( ID );

        /* When. */
        ResponseEntity<?> result = underTest.updateEmployee( ID, dto );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void updateBankAccount_WhenCompanyNotFound() {
        /* Given. */
        final long ID = 123;

        when( companyService.findByID( ID ) ).thenReturn( Optional.empty() );

        BankAccountDTO dto = new BankAccountDTO( ID, null, null, null );
        dto.setId( ID );

        /* When. */
        ResponseEntity<?> result = underTest.updateBankAccount( ID, dto );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void updateBankAccount_WhenBankAccountNotFound() {
        /* Given. */
        final long ID = 123;
        Company company = new Company();
        company.setId( ID );

        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );
        when( bankAccountService.findByID( ID ) ).thenReturn( Optional.empty() );

        BankAccountDTO dto = new BankAccountDTO( ID, null, null, null );
        dto.setId( ID );

        /* When. */
        ResponseEntity<?> result = underTest.updateBankAccount( ID, dto );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void updateBankAccount_WhenWrongAccountNumber() {
        /* Given. */
        final long ID = 123;
        final String ACCOUNT_NUMBER = "TEST";
        Company company = new Company();
        company.setId( ID );

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId( ID );
        bankAccount.setAccontNumber( ACCOUNT_NUMBER );

        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );
        when( bankAccountService.findByID( ID ) ).thenReturn( Optional.of( bankAccount ) );

        BankAccountDTO dto = new BankAccountDTO( ID, "TEST_NUMBER", "TEST_NAME", null );
        dto.setId( ID );
        dto.setAccountNumber( "WRONG" );

        /* When. */
        ResponseEntity<?> result = underTest.updateBankAccount( ID, dto );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.BAD_REQUEST );
    }

    @Test
    void updateBankAccount() {
        /* Given. */
        final long ID = 123;
        final String ACCOUNT_NUMBER = "TEST";
        Company company = new Company();
        company.setId( ID );

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId( ID );
        bankAccount.setAccontNumber( ACCOUNT_NUMBER );

        when( companyService.findByID( ID ) ).thenReturn( Optional.of( company ) );
        when( bankAccountService.findByID( ID ) ).thenReturn( Optional.of( bankAccount ) );

        BankAccountDTO dto = new BankAccountDTO( ID, "TEST_NUMBER", "TEST_NAME", null );
        dto.setId( ID );
        dto.setAccountNumber( ACCOUNT_NUMBER );

        /* When. */
        ResponseEntity<?> result = underTest.updateBankAccount( ID, dto );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void deleteEmployee() {
        /* Given. */

        /* When. */
        ResponseEntity<?> result = underTest.deleteEmployee( 123L, 123L );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.OK );
    }

    @Test
    void deleteBankAccount() {
        /* Given. */
        final long ID = 123;

        /* When. */
        ResponseEntity<?> result = underTest.deleteBankAccount( ID, ID );

        /* Then. */
        assertEquals( result.getStatusCode(), HttpStatus.OK );
    }
}