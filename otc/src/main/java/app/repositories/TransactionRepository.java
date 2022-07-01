package app.repositories;

import app.model.TransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository  extends JpaRepository<TransactionItem, Long> {
}
