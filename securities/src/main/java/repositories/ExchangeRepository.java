package repositories;

import model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    Exchange findByMIC(String MIC);
    Exchange findByAcronym(String acronym);
}
