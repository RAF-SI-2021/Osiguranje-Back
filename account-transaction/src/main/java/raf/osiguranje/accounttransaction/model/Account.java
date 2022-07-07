package raf.osiguranje.accounttransaction.model;

import javax.persistence.*;

@Entity
public class Account {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    protected Long accountNumber;

    @Column(nullable = false)
    protected AccountType accountType;

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
}
