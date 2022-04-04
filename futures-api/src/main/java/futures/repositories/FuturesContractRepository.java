package futures.repositories;

import futures.model.FuturesContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuturesContractRepository extends JpaRepository<FuturesContract, Long> {
}
