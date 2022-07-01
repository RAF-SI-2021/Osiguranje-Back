package app.services;

import app.model.TransactionItem;
import app.model.dto.TransactionItemDTO;
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

    public Optional<TransactionItem> findByID(Long id){
        return transactionRepository.findById(id);
    }

    public void deleteByID(Long id){
        transactionRepository.deleteById(id);
    }

    public void update(TransactionItemDTO transactionItemDTO) {
        Optional<TransactionItem> optionalTransaction = transactionRepository.findById(transactionItemDTO.getId());
        if(optionalTransaction.isEmpty())
            return;

        TransactionItem transactionItem = optionalTransaction.get();
        transactionItem.setAction(transactionItemDTO.getAction());
        transactionItem.setSecurityId(transactionItemDTO.getSecurityId());
        transactionItem.setSecurityType(transactionItemDTO.getSecurityType());
        transactionItem.setAccountId(transactionItemDTO.getAccountId());
        transactionItem.setCurrency(transactionItemDTO.getCurrency());
        transactionItem.setAmount(transactionItemDTO.getAmount());
        transactionItem.setPricePerShare(transactionItemDTO.getPricePerShare());

        transactionRepository.save(transactionItem);
    }
}
