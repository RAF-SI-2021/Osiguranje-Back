package raf.osiguranje.accounttransaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.osiguranje.accounttransaction.model.MarginTransaction;

@Repository
public interface MarginTransactionRepository extends JpaRepository<MarginTransaction, Long> {
}
