package app.services;

import app.model.TransactionItem;
import app.model.dto.TransactionItemDTO;
import app.repositories.TransactionItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionItemService {

    private final TransactionItemRepository transactionItemRepository;

    @Autowired
    public TransactionItemService(TransactionItemRepository transactionItemRepository) {
        this.transactionItemRepository = transactionItemRepository;
    }

    public Optional<TransactionItem> findByID(Long id){
        return transactionItemRepository.findById(id);
    }

    public void deleteByID(Long id){
        transactionItemRepository.deleteById(id);
    }

    public void update(TransactionItemDTO transactionItemDTO) {
        Optional<TransactionItem> optionalTransactionItem = transactionItemRepository.findById(transactionItemDTO.getId());
        if(optionalTransactionItem.isEmpty())
            return;

        TransactionItem transactionItem = optionalTransactionItem.get();
        transactionItem.setTransactionType(transactionItemDTO.getTransactionType());
        transactionItem.setSecurityId(transactionItemDTO.getSecurityId());
        transactionItem.setSecurityType(transactionItemDTO.getSecurityType());
        transactionItem.setAccountId(transactionItemDTO.getAccountId());
        transactionItem.setCurrencyId(transactionItemDTO.getCurrencyId());
        transactionItem.setAmount(transactionItemDTO.getAmount());
        transactionItem.setPricePerShare(transactionItemDTO.getPricePerShare());

        transactionItemRepository.save(transactionItem);
    }
}
