package buyingMarket.repositories;

import buyingMarket.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAll();

    Transaction save(Transaction transaction);

    List<Transaction> findAllByUser(long userId);
}
