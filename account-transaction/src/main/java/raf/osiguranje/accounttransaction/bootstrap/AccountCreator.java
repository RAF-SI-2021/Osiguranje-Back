package raf.osiguranje.accounttransaction.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.AccountType;
import raf.osiguranje.accounttransaction.repositories.AccountRepository;

@Component
public class AccountCreator implements CommandLineRunner {

    private AccountRepository repository;

    @Autowired
    public AccountCreator(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        Account account = new Account(AccountType.CASH);
        this.repository.save(account);

        account = new Account(AccountType.MARGINS);
        this.repository.save(account);
        System.out.println("Created INIT Accounts");
    }
}
