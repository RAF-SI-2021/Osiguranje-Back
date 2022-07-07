package raf.osiguranje.accounttransaction.model;

import raf.osiguranje.accounttransaction.model.dto.SecurityType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MarginAccount {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    protected Long id;
    @Column(nullable = false)
    protected AccountType accountType;
    @Column
    private Long accountId;
    @Column
    private Long currencyId;
    @Column
    private SecurityType securityType;
    @Column
    private BigDecimal investedFunds;
    @Column
    private BigDecimal loanValue;
    @Column
    private BigDecimal maintenanceMargin;
    @Column
    private Boolean marginCall;

    public MarginAccount() {
        this.accountType = AccountType.MARGIN;
    }

    public MarginAccount(Long id, AccountType accountType, Long accountId, Long currencyId, SecurityType securityType, BigDecimal investedFunds, BigDecimal loanValue, BigDecimal maintenanceMargin, Boolean marginCall) {
        this.id = id;
        this.accountType = accountType;
        this.accountId = accountId;
        this.currencyId = currencyId;
        this.securityType = securityType;
        this.investedFunds = investedFunds;
        this.loanValue = loanValue;
        this.maintenanceMargin = maintenanceMargin;
        this.marginCall = marginCall;
    }

    public Long getAccountNumber() {
        return id;
    }

    public void setAccountNumber(Long accountNumber) {
        this.id = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public SecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }

    public BigDecimal getInvestedFunds() {
        return investedFunds;
    }

    public void setInvestedFunds(BigDecimal investedFunds) {
        this.investedFunds = investedFunds;
    }

    public BigDecimal getLoanValue() {
        return loanValue;
    }

    public void setLoanValue(BigDecimal loanValue) {
        this.loanValue = loanValue;
    }

    public BigDecimal getMaintenanceMargin() {
        return maintenanceMargin;
    }

    public void setMaintenanceMargin(BigDecimal maintenanceMargin) {
        this.maintenanceMargin = maintenanceMargin;
    }

    public Boolean getMarginCall() {
        return marginCall;
    }

    public void setMarginCall(Boolean marginCall) {
        this.marginCall = marginCall;
    }
}
