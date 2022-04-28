package buyingMarket.model;

import crudApp.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long recieptId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Transaction> transactions;

    public Receipt(User user) {
        this.user = user;
        this.transactions = new ArrayList<>();
    }

    public Receipt(User user, Collection<Transaction> transactions) {
        this.user = user;
        this.transactions = transactions;
    }
}
