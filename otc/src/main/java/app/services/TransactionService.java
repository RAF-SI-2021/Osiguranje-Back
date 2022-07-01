package app.services;

import app.model.Company;
import app.model.Transaction;
import app.model.dto.CompanyDTO;
import app.model.dto.TransactionDTO;
import app.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Optional<Transaction> findByID(Long id){
        return transactionRepository.findById(id);
    }

    public void deleteByID(Long id){
        transactionRepository.deleteById(id);
    }

    public void update(TransactionDTO transactionDTO) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionDTO.getId());
        if(optionalTransaction.isEmpty())
            return;

        Transaction transaction = optionalTransaction.get();
        transaction.setAction(transactionDTO.getAction());
        transaction.setSecurityId(transactionDTO.getSecurityId());
        transaction.setSecurityType(transactionDTO.getSecurityType());
        transaction.setAccountId(transactionDTO.getAccountId());
        transaction.setCurrency(transactionDTO.getCurrency());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setPricePerShare(transactionDTO.getPricePerShare());

        transactionRepository.save(transaction);
    }
}
