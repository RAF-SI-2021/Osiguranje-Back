package raf.osiguranje.accounttransaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.AccountBalance;
import raf.osiguranje.accounttransaction.model.BalanceId;

import java.util.List;

public interface BalanceRepository extends JpaRepository<AccountBalance, BalanceId> {

    List<AccountBalance> findAccountBalanceByAccount(Account account);
    List<AccountBalance> findAccountBalanceBySecurityId(Long securityId);
    List<AccountBalance> findAccountBalanceByAccountAndSecurityId(Account account,Long securityId);
}
