package raf.osiguranje.accounttransaction.model;

import raf.osiguranje.accounttransaction.model.dto.SecurityType;
import raf.osiguranje.accounttransaction.model.dto.TransactionType;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class MarginTransaction extends TransactionBase {
    @Column
    private BigDecimal investedFunds;
    @Column
    private BigDecimal loanValue;
    @Column
    private BigDecimal maintenanceMargin;
    @Column
    private BigDecimal interestValue;

    public MarginTransaction() {}

    public MarginTransaction(Long accountId, Long orderId, Long userId, Long currencyId, Long securityId, SecurityType securityType, String text, TransactionType transactionType, BigDecimal investedFunds, BigDecimal loanValue, BigDecimal maintenanceMargin, BigDecimal interestValue) {
        super(accountId, orderId, userId, currencyId, securityId, securityType, text, transactionType);
        this.investedFunds = investedFunds;
        this.loanValue = loanValue;
        this.maintenanceMargin = maintenanceMargin;
        this.interestValue = interestValue;
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

    public BigDecimal getInterestValue() {
        return interestValue;
    }

    public void setInterestValue(BigDecimal interestValue) {
        this.interestValue = interestValue;
    }
}
