package raf.osiguranje.accounttransaction.model;


import accounts.TransactionDto;
import accounts.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import market.OrderDto;


import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long accountId;

    @Column
    private LocalDateTime timestamp=LocalDateTime.now();

    @Column
    private Long orderId;

    @Column
    private Long userId;

    @Column
    private Long currencyId;

    @Column
    private String text;

    @Column
    private int payment;

    @Column
    private int payout;

    @Column
    private int reserve;

    @Column
    private int usedReserve;

    @Column
    private TransactionType transactionType;

    public Transaction(){
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(Long accountId, Long orderId, Long userId, Long currencyId, int payment, int payout, int reserve, int usedReserve,String text,TransactionType transactionType) {
        this.accountId = accountId;
        this.timestamp = LocalDateTime.now();
        this.orderId = orderId;
        this.userId = userId;
        this.currencyId = currencyId;
        this.payment = payment;
        this.payout = payout;
        this.reserve = reserve;
        this.usedReserve = usedReserve;
        this.transactionType = transactionType;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", timestamp=" + timestamp +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", currencyId=" + currencyId +
                ", text='" + text + '\'' +
                ", payment=" + payment +
                ", payout=" + payout +
                ", reserve=" + reserve +
                ", usedReserve=" + usedReserve +
                '}';
    }

    public TransactionDto getDto(){
        OrderDto ord = new OrderDto();
        ord.setOrderId(this.orderId);
        return new TransactionDto(this.id,this.accountId,this.timestamp,ord,this.userId,this.currencyId,
                this.text,this.payment,this.payout,this.reserve,this.usedReserve,transactionType);
    }

}
