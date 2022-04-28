package buyingMarket.mappers;

import buyingMarket.model.Transaction;
import buyingMarket.model.dto.TransactionCreateDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public Transaction transactionCreateDtoToTransaction(TransactionCreateDto dto) {
        return new Transaction(dto.getUserId(), dto.getTime(), dto.getSecurity(), dto.getPrice(), dto.getVolume(), dto.getOrderType());
    }
}
