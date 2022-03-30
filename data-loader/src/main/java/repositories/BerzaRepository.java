package repositories;

import models.Berza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BerzaRepository extends JpaRepository<Berza, Long> {

    @Query( value = "SELECT b FROM Berza b WHERE b.naziv LIKE '%' || :marketName || '%'" )
    Collection<Berza> findAllByName( @Param( "marketName" ) String marketName );

    Optional<Berza> findByNaziv( String naziv );

    Optional<Berza> findByOznaka( String oznaka );
}
