package buyingMarket.mappers;

import buyingMarket.model.Transaction;
import buyingMarket.model.dto.TransactionDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public Transaction transactionCreateDtoToTransaction(TransactionDto dto) {
        return new Transaction(dto.getUserId(), dto.getTime(), dto.getSecurity(), dto.getPrice(), dto.getVolume(), dto.getOrderType(), dto.isAllOrNone(), dto.isMargin());
    }
}
