package raf.osiguranje.accounttransaction.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.AccountType;
import raf.osiguranje.accounttransaction.repositories.AccountRepository;

import java.util.List;

@NoArgsConstructor
@Service
public class AccountService {

    private AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }


    public Account createNewAccount(AccountType type){
        return repository.save(new Account(type));
    }

    public Account createNewAccount(){
        return repository.save(new Account());
    }

    public void deleteAccount(Account account){
        repository.delete(account);
    }

    public List<Account> getAllAccounts(){
        return repository.findAll();
    }

    public Account findAccountById(Long id){
        return repository.findAccountByAccountNumber(id);
    }

    public List<Account> findAccountByAccountType(AccountType type) {
        return repository.findAccountByAccountType(type);
    }
}
