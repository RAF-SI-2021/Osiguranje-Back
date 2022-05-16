package app.repositories;

import app.model.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RegionRepositoryTest {

    @Autowired
    private RegionRepository underTest;

    @Test
    void findByCode() {
        /* Given. */
        final String CODE = "SRB";
        Region region = new Region();
        region.setCode( CODE );

        underTest.save( region );

        /* When. */
        Region testRegion = underTest.findByCode( CODE );

        /* Then. */
        assertEquals( testRegion.getCode(), CODE );
    }

    @Test
    void findByName() {
        /* Given. */
        final String NAME = "Serbia";
        Region region = new Region();
        region.setName( NAME );

        underTest.save( region );

        /* When. */
        Region testRegion = underTest.findByName( NAME );

        /* Then. */
        assertEquals( testRegion.getName(), NAME );
    }
}