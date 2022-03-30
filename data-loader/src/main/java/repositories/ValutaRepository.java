package repositories;

import models.Valuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValutaRepository extends JpaRepository<Valuta, Long> {

    Optional<Valuta> findByIsoCode( String isoCode );
    Optional<Valuta> findByDrzava( String drzava );

}
