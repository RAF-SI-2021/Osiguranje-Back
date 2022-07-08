package app.model;

import app.model.dto.TransactionItemDTO;
import app.model.api.SecurityType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private TransactionType transactionType;
    @Column
    private Long securityId;
    @Column
    private SecurityType securityType;
    @Column
    private Long accountId;
    @Column
    private Long currencyId;
    @Column
    private int amount;
    @Column
    private double pricePerShare;

    public TransactionItem(TransactionItemDTO transactionItemDTO) {
        this.transactionType = transactionItemDTO.getTransactionType();
        this.securityId = transactionItemDTO.getSecurityId();
        this.securityType = transactionItemDTO.getSecurityType();
        this.accountId = transactionItemDTO.getAccountId();
        this.currencyId = transactionItemDTO.getCurrencyId();
        this.amount = transactionItemDTO.getAmount();
        this.pricePerShare = transactionItemDTO.getPricePerShare();
    }
}
