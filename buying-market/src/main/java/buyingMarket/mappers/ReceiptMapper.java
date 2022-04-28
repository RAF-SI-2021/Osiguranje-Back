package buyingMarket.mappers;

import buyingMarket.model.Receipt;
import buyingMarket.model.dto.ReceiptCreateDto;
import org.springframework.stereotype.Component;

@Component
public class ReceiptMapper {
    public Receipt receiptCreateDtoToReceipt(ReceiptCreateDto dto) {
        return new Receipt(dto.getUser(), dto.getTransactions());
    }
}
