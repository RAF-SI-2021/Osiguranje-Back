package buyingMarket.services;

import buyingMarket.model.Transaction;
import buyingMarket.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTransactions(){
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByUser(long userId){
        return transactionRepository.findAllByUser(userId);
    }

    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public List<Transaction> save(List<Transaction> transactions){
        return transactionRepository.saveAll(transactions);
    }
}
