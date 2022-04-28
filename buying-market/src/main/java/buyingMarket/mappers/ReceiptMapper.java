package buyingMarket.mappers;

import buyingMarket.model.Receipt;
import buyingMarket.model.dto.ReceiptDto;
import org.springframework.stereotype.Component;

@Component
public class ReceiptMapper {
    public Receipt receiptCreateDtoToReceipt(ReceiptDto dto) {
        return new Receipt(dto.getUser(), dto.getTransactions());
    }
}
