package app.services;

import app.model.BankAccount;
import app.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public void save(BankAccount bankAccount){
        bankAccountRepository.save(bankAccount);
    }

    public void deleteByID(Long id){
        bankAccountRepository.deleteById(id);
    }

}
