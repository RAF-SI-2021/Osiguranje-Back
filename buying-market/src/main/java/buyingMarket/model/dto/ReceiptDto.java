package buyingMarket.model.dto;

import buyingMarket.model.Transaction;
import crudApp.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class ReceiptDto {
    private long userId;
    private Collection<Transaction> transactions;

    public ReceiptDto(long userId) {
        this.userId = userId;
    }

    public ReceiptDto(long userId, Collection<Transaction> transactions) {
        this.userId = userId;
        this.transactions = transactions;
    }
}
