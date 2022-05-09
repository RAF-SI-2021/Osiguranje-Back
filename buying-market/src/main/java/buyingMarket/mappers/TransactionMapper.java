package buyingMarket.mappers;

import buyingMarket.model.Transaction;
import buyingMarket.model.dto.TransactionDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public Transaction transactionCreateDtoToTransaction(TransactionDto dto) {
        return new Transaction(dto.getReceiptId(), dto.getTime(), dto.getSecurity(), dto.getPrice(), dto.getVolume());
    }
}
