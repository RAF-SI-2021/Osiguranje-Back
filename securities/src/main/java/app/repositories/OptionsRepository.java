package app.repositories;

import app.model.StockOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsRepository extends JpaRepository<StockOption, Long> {
    List<StockOption> findAll();
    StockOption save(StockOption option);
}
