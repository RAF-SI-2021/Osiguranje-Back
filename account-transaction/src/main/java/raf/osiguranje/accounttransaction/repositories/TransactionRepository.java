package raf.osiguranje.accounttransaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.osiguranje.accounttransaction.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findAllByAccountId(Long input);

    List<Transaction> findAllByForexId(Long input);

    List<Transaction> findAllByUserId(Long input);

    List<Transaction> findAllByOrderId(Long input);

}
