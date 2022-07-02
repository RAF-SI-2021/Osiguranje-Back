package app.model.dto;

import app.model.Action;
import app.model.TransactionItem;
import app.model.api.SecurityType;
import lombok.Getter;

@Getter
public class TransactionItemDTO {

    private Long id;
    private Action action;
    private Long securityId;
    private SecurityType securityType;
    private Long accountId;
    private Long currencyId;
    private int amount;
    private double pricePerShare;

    public TransactionItemDTO(TransactionItem transactionItem) {
        this.id = transactionItem.getId();
        this.action = transactionItem.getAction();
        this.securityId = transactionItem.getSecurityId();
        this.securityType = transactionItem.getSecurityType();
        this.accountId = transactionItem.getAccountId();
        this.currencyId = transactionItem.getCurrencyId();
        this.amount = transactionItem.getAmount();
        this.pricePerShare = transactionItem.getPricePerShare();
    }
}
