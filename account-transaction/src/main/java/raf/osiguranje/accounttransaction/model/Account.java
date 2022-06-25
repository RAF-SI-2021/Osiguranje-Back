package raf.osiguranje.accounttransaction.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import raf.osiguranje.accounttransaction.model.helpers.AccountType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Account {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long accountNumber;

    @Column(nullable = false)
    private AccountType accountType;

    public Account(AccountType accountType) {
        this.accountType = accountType;
    }

    public Account() {
        this.accountType = AccountType.CASH;
    }

}
