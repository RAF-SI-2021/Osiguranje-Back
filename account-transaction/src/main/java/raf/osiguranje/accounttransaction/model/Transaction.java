package raf.osiguranje.accounttransaction.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.osiguranje.accounttransaction.model.dto.TransactionDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long accountId;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

    @Column
    private Long orderId;

    @Column
    private Long userId;

    @Column
    private Long forexId;

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


    public Transaction(Long accountId, Long orderId, Long userId, Long forexId, int payment, int payout, int reserve, int usedReserve) {
        this.accountId = accountId;
        this.timestamp = LocalDateTime.now();
        this.orderId = orderId;
        this.userId = userId;
        this.forexId = forexId;
        this.payment = payment;
        this.payout = payout;
        this.reserve = reserve;
        this.usedReserve = usedReserve;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", timestamp=" + timestamp +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", forexId=" + forexId +
                ", text='" + text + '\'' +
                ", payment=" + payment +
                ", payout=" + payout +
                ", reserve=" + reserve +
                ", usedReserve=" + usedReserve +
                '}';
    }

    public TransactionDTO getDto(){
        return new TransactionDTO(this.accountId,this.timestamp,this.orderId,this.userId,this.forexId,
                this.text,this.payment,this.payout,this.reserve,this.usedReserve);
    }

}
