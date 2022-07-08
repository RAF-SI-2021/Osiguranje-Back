package app.repositories;

import app.model.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    CompanyRepository underTest;

    @Test
    void findAllByName() {
        /* Given. */
        final String NAME = "TEST_NAME";
        Company company1 = new Company();
        company1.setName( NAME );

        Company company2 = new Company();
        company1.setName( NAME );

        underTest.save( company1 );
        // underTest.save( company2 );

        /* When. */
        List<Company> result = underTest.findAllByName( NAME );

        /* Then. */
        assertEquals( result.get( 0 ).getName(), NAME );
        // assertEquals( result.get( 1 ).getName(), NAME );

    }

    @Test
    void findAllByRegistrationID() {
        /* Given. */
        final long REGID = -1;
        Company company1 = new Company();
        company1.setRegistrationID( REGID );

        underTest.save( company1 );

        /* When. */
        List<Company> result = underTest.findAllByRegistrationID( REGID );

        /* Then. */
        assertEquals( result.get( 0 ).getRegistrationID(), REGID );
    }

    @Test
    void findAllByTaxID() {
        /* Given. */
        final long TAXID = -1;
        Company company1 = new Company();
        company1.setTaxID( TAXID );

        underTest.save( company1 );

        /* When. */
        List<Company> result = underTest.findAllByTaxID( TAXID );

        /* Then. */
        assertEquals( result.get( 0 ).getTaxID(), TAXID );
    }
}