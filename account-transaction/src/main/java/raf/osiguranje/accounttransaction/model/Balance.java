package raf.osiguranje.accounttransaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.osiguranje.accounttransaction.model.dto.BalanceDTO;
import raf.osiguranje.accounttransaction.model.dto.SecurityType;

import javax.persistence.*;

@IdClass(BalanceId.class)
public class Balance {

    @Id
    @Column(name = "account_account_number",nullable = false)
    protected Long accountId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account", nullable = false)
    private Account account;

    @Id
    @Column(name = "securityId", nullable = false)
    private Long securityId;

    @Column
    private SecurityType securityType;

    @Column
    private Integer amount;

    @Column
    private Integer reserved;

    public Balance() {
    }

    public Balance(Account account, Long securityId, SecurityType securityType, Integer amount) {
        this.accountId = account.getAccountNumber();
        this.account = account;
        this.securityId = securityId;
        this.securityType = securityType;
        this.amount = amount;
        this.reserved = 0;
    }

    public Integer getAvailable(){
        return this.amount - this.reserved;
    }

    public BalanceId getBalanceId(){
        return new BalanceId(account.getAccountNumber(),securityId);
    }

    public BalanceDTO getDto(){
        System.out.println(account);
        return new BalanceDTO(accountId,securityId,securityType,amount,reserved,getAvailable());
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getSecurityId() {
        return securityId;
    }

    public void setSecurityId(Long securityId) {
        this.securityId = securityId;
    }

    public SecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "account=" + accountId +
                ", securityId=" + securityId +
                ", securityType=" + securityType +
                ", amount=" + amount +
                ", reserved=" + reserved +
                '}';
    }
}
