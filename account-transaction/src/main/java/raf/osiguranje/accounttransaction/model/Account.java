package raf.osiguranje.accounttransaction.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
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

    public Account(Long accountNumber, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", accountType=" + accountType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber) && accountType == account.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accountType);
    }
}
