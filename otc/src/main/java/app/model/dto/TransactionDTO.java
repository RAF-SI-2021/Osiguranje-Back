package app.model.dto;

import app.model.Action;
import app.model.Transaction;
import app.model.securities.SecurityType;
import lombok.Getter;

@Getter
public class TransactionDTO {

    private Long id;
    private Action action;
    private String securityId;
    private SecurityType securityType;
    private Long accountId;
    private String currency;
    private Long amount;
    private double pricePerShare;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.action = transaction.getAction();
        this.securityId = transaction.getSecurityId();
        this.securityType = transaction.getSecurityType();
        this.accountId = transaction.getAccountId();
        this.currency = transaction.getCurrency();
        this.amount = transaction.getAmount();
        this.pricePerShare = transaction.getPricePerShare();
    }
}
