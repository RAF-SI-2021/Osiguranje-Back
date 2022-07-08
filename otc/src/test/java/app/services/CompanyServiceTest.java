package app.services;

import app.model.BankAccount;
import app.model.Company;
import app.model.Employee;
import app.model.dto.CompanyDTO;
import app.repositories.BankAccountRepository;
import app.repositories.CompanyRepository;
import app.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompanyServiceTest {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CompanyService underTest;

    @Test
    void save() {
        /* Given. */
        Company company = new Company();

        /* When. */
        Company saved = underTest.save( company );
        Optional<Company> result = companyRepository.findById( saved.getId() );

        /* Then. */
        assertTrue( result.isPresent() );
        company = result.get();
        assertEquals( company.getId(), saved.getId() );
    }

    @Test
    void findAll() {
        /* Given. */
        final int size = companyRepository.findAll().size();
        companyRepository.save( new Company() );

        /* When. */
        final int resultSize = underTest.findAll().size();

        /* Then. */
        assertEquals( resultSize, size + 1 );
    }

    @Test
    void findByID() {
        /* Given. */
        Company company = companyRepository.save( new Company() );
        final long ID = company.getId();

        /* When. */
        Optional<Company> result = underTest.findByID( ID );

        /* Then. */
        assertTrue( result.isPresent() );
        company = result.get();
        assertEquals( company.getId(), ID );
    }

    @Test
    void findByRegistrationID() {
        /* Given. */
        Company company = new Company();
        final long REGID = -1;
        company.setRegistrationID( REGID );

        companyRepository.save( company );

        /* When. */
        List<Company> result = underTest.findByRegistrationID( REGID );

        /* Then. */
        for( Company c: result )
        {
            assertEquals( c.getRegistrationID(), REGID );
        }
    }

    @Test
    void findByName() {
        /* Given. */
        Company company = new Company();
        final String NAME = "TEST_NAME";
        company.setName( NAME );

        companyRepository.save( company );

        /* When. */
        List<Company> result = underTest.findByName( NAME );

        /* Then. */
        for( Company c: result )
        {
            assertEquals( c.getName(), NAME );
        }
    }

    @Test
    void findByTaxID() {
        /* Given. */
        Company company = new Company();
        final long TAXID = -1;
        company.setTaxID( TAXID );

        /* When. */
        underTest.save( company );
        List<Company> result = companyRepository.findAllByTaxID( TAXID );

        /* Then. */
        for( Company c: result )
        {
            assertEquals( c.getTaxID(), TAXID );
        }
    }

    @Test
    void deleteByID() {
        /* Given. */
        Company company = companyRepository.save( new Company() );
        final long ID = company.getId();

        /* When. */
        underTest.deleteByID( ID );
        Optional<Company> result = underTest.findByID( ID );

        /* Then. */
        assertFalse( result.isPresent() );
    }

    @Test
    @Transactional
    void update() {
        /* Given. */
        String name = "TEST_NAME";
        Company company = new Company();
        company.setName( name );
        company.setEmployees( new ArrayList<>() );
        company.setBankAccounts( new ArrayList<>() );
        company = companyRepository.save( company );

        Employee employee = new Employee();
        employee.setCompany( company );
        employeeRepository.save( employee );

        BankAccount bankAccount = new BankAccount();
        bankAccount.setCompany( company );
        bankAccountRepository.save( bankAccount );

        company = companyRepository.findById( company.getId() ).get();

        name = "UPDATED_NAME";
        final long ID = company.getId();

        CompanyDTO companyDTO = new CompanyDTO( company );
        companyDTO.setName( name );

        /* When. */
        underTest.update( companyDTO );
        Optional<Company> result = companyRepository.findById( ID );

        /* Then. */
        assertTrue( result.isPresent() );
        assertEquals( result.get().getName(), name );
    }
}