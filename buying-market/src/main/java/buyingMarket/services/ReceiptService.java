package buyingMarket.services;

import buyingMarket.mappers.ReceiptMapper;
import buyingMarket.mappers.TransactionMapper;
import buyingMarket.model.Receipt;
import buyingMarket.model.Transaction;
import buyingMarket.model.dto.ReceiptDto;
import buyingMarket.model.dto.TransactionDto;
import buyingMarket.repositories.ReceiptRepository;
import buyingMarket.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {
    private final TransactionRepository transactionRepository;
    private final ReceiptRepository receiptRepository;
    private final TransactionMapper transactionMapper;
    private final ReceiptMapper receiptMapper;

    @Autowired
    public ReceiptService(TransactionRepository transactionRepository, ReceiptRepository receiptRepository, TransactionMapper transactionMapper, ReceiptMapper receiptMapper) {
        this.transactionRepository = transactionRepository;
        this.receiptRepository = receiptRepository;
        this.transactionMapper = transactionMapper;
        this.receiptMapper = receiptMapper;
    }

    public List<Receipt> findReceiptByUser(long userId){
        return receiptRepository.findAllByUser(userId);
    }

    public Receipt createReceipt(ReceiptDto dto) {
        return receiptRepository.save(receiptMapper.receiptCreateDtoToReceipt(dto));
    }

    public Receipt addTransaction(long receiptId, TransactionDto dto){
        Transaction transaction = transactionMapper.transactionCreateDtoToTransaction(dto);
        transactionRepository.save(transaction);
        Optional<Receipt> receipt = receiptRepository.findById(receiptId);
        if (receipt.isPresent()) {
            Receipt r = receipt.get();
            r.getTransactions().add(transaction);
            receiptRepository.save(r);
            return r;
        }
        return null;
    }
}
