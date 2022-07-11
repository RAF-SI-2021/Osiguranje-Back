package raf.osiguranje.accounttransaction.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MarginTransactionDto {
    private Long accountId;
    private LocalDateTime timestamp;
    private Long orderId;
    private Long userId;
    private Long currencyId;
    private Long securityId;
    private SecurityType securityType;
    private String text;
    private BigDecimal deposit;
    private BigDecimal loanValue;
    private BigDecimal maintenanceMargin;
    private BigDecimal interestValue;
    private TransactionType transactionType;

    public MarginTransactionDto() {}

    public MarginTransactionDto(Long accountId, LocalDateTime timestamp, Long orderId, Long userId, Long currencyId, Long securityId, SecurityType securityType, String text, BigDecimal deposit, BigDecimal loanValue, BigDecimal maintenanceMargin, BigDecimal interestValue, TransactionType transactionType) {
        this.accountId = accountId;
        this.timestamp = timestamp;
        this.orderId = orderId;
        this.userId = userId;
        this.currencyId = currencyId;
        this.securityId = securityId;
        this.securityType = securityType;
        this.text = text;
        this.deposit = deposit;
        this.loanValue = loanValue;
        this.maintenanceMargin = maintenanceMargin;
        this.interestValue = interestValue;
        this.transactionType = transactionType;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
