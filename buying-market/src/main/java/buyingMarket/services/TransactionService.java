package buyingMarket.services;

import buyingMarket.mappers.TransactionMapper;
import buyingMarket.model.Transaction;
import buyingMarket.model.dto.TransactionCreateDto;
import buyingMarket.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByUser(long userId){
        return transactionRepository.findAllByUser(userId);
    }

    public Transaction save(TransactionCreateDto dto){
        return transactionRepository.save(transactionMapper.transactionCreateDtoToTransaction(dto));
    }
}
