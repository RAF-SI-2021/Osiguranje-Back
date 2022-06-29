package otc.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionID;
    @Column
    private Action action;
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "registration_ID", nullable = false)
//    private Security security;
//    @Column
//    private Account account;
    @Column
    private String currency;
    @Column
    private Long amount;
    @Column
    private double pricePerShare;


    private enum Action{
        BUY, SELL
    }
}
