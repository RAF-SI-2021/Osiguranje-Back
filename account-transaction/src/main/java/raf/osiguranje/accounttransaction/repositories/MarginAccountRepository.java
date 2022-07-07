package raf.osiguranje.accounttransaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.osiguranje.accounttransaction.model.MarginAccount;

@Repository
public interface MarginAccountRepository extends JpaRepository<MarginAccount, Long> {
}
