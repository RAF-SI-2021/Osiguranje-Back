package app.services;

import app.model.BankAccount;
import app.model.dto.BankAccountDTO;
import app.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount save(BankAccount bankAccount){
        return bankAccountRepository.save(bankAccount);
    }

    public void deleteByID(Long id){
        bankAccountRepository.deleteById(id);
    }

    public Optional<BankAccount> findByID(Long id) {
        return bankAccountRepository.findById(id);
    }

    public BankAccount update(BankAccount bankAccount, BankAccountDTO bankAccountDTO) {
        bankAccount.setBankName(bankAccountDTO.getBankName());
        return save(bankAccount);
    }
}
