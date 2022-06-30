package raf.osiguranje.accounttransaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.AccountType;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findAccountByAccountNumber(Long accountId);
    List<Account> findAccountByAccountType(AccountType accountType);
}
