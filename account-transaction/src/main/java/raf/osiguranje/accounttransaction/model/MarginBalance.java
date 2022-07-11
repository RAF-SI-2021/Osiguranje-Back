package raf.osiguranje.accounttransaction.model;

import raf.osiguranje.accounttransaction.model.dto.SecurityType;

import javax.persistence.*;

@IdClass(BalanceId.class)
@Entity
public class MarginBalance {
    @Id
    private Long marginAccountId;
    @Id
    private Long securityId;
    @Id
    private SecurityType securityType;
    @ManyToOne
    private MarginAccount marginAccount;
    @Column
    private Integer amount;
    @Version
    private int version = 0;

    public MarginBalance() {}

    public MarginBalance(Long marginAccountId, Long securityId, SecurityType securityType, MarginAccount marginAccount, Integer amount, int version) {
        this.marginAccountId = marginAccountId;
        this.securityId = securityId;
        this.securityType = securityType;
        this.marginAccount = marginAccount;
        this.amount = amount;
        this.version = version;
    }

    public Long getMarginAccountId() {
        return marginAccountId;
    }

    public void setMarginAccountId(Long marginAccountId) {
        this.marginAccountId = marginAccountId;
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

    public MarginAccount getMarginAccount() {
        return marginAccount;
    }

    public void setMarginAccount(MarginAccount marginAccount) {
        this.marginAccount = marginAccount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
