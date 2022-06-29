package raf.osiguranje.accounttransaction.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Account {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
