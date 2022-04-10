package repositories;
import model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;


@Repository
public interface StocksRepository extends JpaRepository<Stock, Long> {
    List<Stock> findAll();

    Stock save(Stock stock);

    List<Stock> findStockBySymbol(String symbol);

    @Query(value = "SELECT * FROM stocks WHERE date >= ?1 AND date <= ?2", nativeQuery = true)
    List<Stock> findByDateWindow(Date startDate, Date endDate);
}
