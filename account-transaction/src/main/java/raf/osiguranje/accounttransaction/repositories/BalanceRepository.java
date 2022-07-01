package raf.osiguranje.accounttransaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.Balance;
import raf.osiguranje.accounttransaction.model.BalanceId;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, BalanceId> {


    @Override
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<Balance> findById(BalanceId balanceId);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    List<Balance> findBalanceByAccount(Account account);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    List<Balance> findBalanceBySecurityId(Long securityId);

    List<Balance> findBalanceByAccountIdAndSecurityId(Long accountId,Long securityId);
}
