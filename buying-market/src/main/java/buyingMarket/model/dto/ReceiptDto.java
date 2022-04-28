package buyingMarket.model.dto;

import buyingMarket.model.Transaction;
import crudApp.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class ReceiptDto {
    private User user;
    private Collection<Transaction> transactions;

    public ReceiptDto(User user, Collection<Transaction> transactions) {
        this.user = user;
        this.transactions = transactions;
    }
}
