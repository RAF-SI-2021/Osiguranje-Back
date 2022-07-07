package raf.osiguranje.accounttransaction.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.AccountType;
import raf.osiguranje.accounttransaction.model.Balance;
import raf.osiguranje.accounttransaction.model.dto.SecurityType;
import raf.osiguranje.accounttransaction.repositories.AccountRepository;
import raf.osiguranje.accounttransaction.repositories.BalanceRepository;

import java.util.List;

@Component
public class DataBootstrapper implements CommandLineRunner {

    private AccountRepository accountRepository;
    private BalanceRepository balanceRepository;

    @Autowired
    public DataBootstrapper(AccountRepository accountRepository, BalanceRepository balanceRepository) {
        this.accountRepository = accountRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public void run(String... args) {
        createAccounts();
        createBalance();
    }

    private void createAccounts(){
        List<Account> allAccounts = accountRepository.findAll();
        if(allAccounts.isEmpty()) {
            Account account = new Account(AccountType.CASH);
            System.out.println(account);
            this.accountRepository.save(account);

            account = new Account(AccountType.MARGIN);
            this.accountRepository.save(account);
        } else {
            System.out.println(allAccounts);
        }
        System.out.println("Created INIT Accounts");
    }

    private void createBalance(){
        try {
            Account account = accountRepository.findAccountByAccountNumber(1L);
            Balance balance = new Balance(account,14L, SecurityType.CURRENCY,100000); // USD balans
            balanceRepository.save(balance);

            balance = new Balance(account,56L, SecurityType.CURRENCY,100000); // EUR balans
            balanceRepository.save(balance);

        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        System.out.println("Created INIT Balances");
    }
}
