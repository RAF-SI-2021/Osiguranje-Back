package buyingMarket.services;

import buyingMarket.model.Reciept;
import buyingMarket.model.Transaction;
import buyingMarket.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RecieptService {
    public final TransactionRepository transactionRepository;

    @Autowired
    public RecieptService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Reciept getReciept(long userId){
        List<Transaction> userTransactions = transactionRepository.findAllByUser(userId);

        return new Reciept(userId, userTransactions);
    }

    public Reciept addTransaction(Transaction transaction){
        transactionRepository.save(transaction);

        return getReciept(transaction.getUserId());
    }
}
