package buyingMarket.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long receiptId;
    @Column
    private long userId;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Transaction> transactions;

    public Receipt(long userId) {
        this.userId = userId;
        this.transactions = new ArrayList<>();
    }

    public Receipt(long userId, Collection<Transaction> transactions) {
        this.userId = userId;
        this.transactions = transactions;
    }
}
