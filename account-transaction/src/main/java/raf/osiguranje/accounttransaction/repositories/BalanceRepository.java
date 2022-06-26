package raf.osiguranje.accounttransaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.Balance;
import raf.osiguranje.accounttransaction.model.BalanceId;

import java.util.List;

public interface BalanceRepository extends JpaRepository<Balance, BalanceId> {

    List<Balance> findAccountBalanceByAccount(Account account);
    List<Balance> findAccountBalanceBySecurityId(Long securityId);
    List<Balance> findAccountBalanceByAccountAndSecurityId(Account account, Long securityId);
}
