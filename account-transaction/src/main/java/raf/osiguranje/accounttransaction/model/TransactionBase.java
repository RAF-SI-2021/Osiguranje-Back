package raf.osiguranje.accounttransaction.model;

import raf.osiguranje.accounttransaction.model.dto.SecurityType;
import raf.osiguranje.accounttransaction.model.dto.TransactionType;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Entity
public class TransactionBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected Long accountId;

    @Column
    protected final LocalDateTime timestamp = LocalDateTime.now();

    @Column
    protected Long orderId;

    @Column
    protected Long userId;

    @Column
    protected Long currencyId;

    @Column
    protected Long securityId;

    @Column
    protected SecurityType securityType;

    @Column
    protected String text;

    @Column
    protected TransactionType transactionType;

    public TransactionBase() {

    }

    public TransactionBase(Long accountId, Long orderId, Long userId, Long currencyId, Long securityId, SecurityType securityType, String text, TransactionType transactionType) {
        this.accountId = accountId;
        this.orderId = orderId;
        this.userId = userId;
        this.currencyId = currencyId;
        this.securityId = securityId;
        this.securityType = securityType;
        this.text = text;
        this.transactionType = transactionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
