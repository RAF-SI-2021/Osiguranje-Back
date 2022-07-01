package app.model;

import app.model.dto.TransactionDTO;
import app.model.securities.SecurityType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Action action;
    @Column
    private String securityId;
    @Column
    private SecurityType securityType;
    @Column
    private Long accountId;
    @Column
    private String currency;
    @Column
    private Long amount;
    @Column
    private double pricePerShare;

    public Transaction(TransactionDTO transactionDTO) {
        this.action = transactionDTO.getAction();
        this.securityId = transactionDTO.getSecurityId();
        this.securityType = transactionDTO.getSecurityType();
        this.accountId = transactionDTO.getAccountId();
        this.currency = transactionDTO.getCurrency();
        this.amount = transactionDTO.getAmount();
        this.pricePerShare = transactionDTO.getPricePerShare();
    }
}
