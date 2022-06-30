package raf.osiguranje.accounttransaction.repositories;

import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.AccountType;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findAccountByAccountNumber(Long accountId);
    List<Account> findAccountByAccountType(AccountType accountType);
}
