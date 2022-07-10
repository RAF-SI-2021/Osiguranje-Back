package raf.osiguranje.accounttransaction.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.osiguranje.accounttransaction.model.dto.OrderDto;
import raf.osiguranje.accounttransaction.model.dto.TransactionDTO;
import raf.osiguranje.accounttransaction.model.dto.TransactionType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public TransactionDTO getDto(){
        OrderDto ord = new OrderDto();
        ord.setOrderId(this.orderId);
        return new TransactionDTO(this.id,this.accountId,this.timestamp,ord,this.userId,this.currencyId,
                this.text,this.payment,this.payout,this.reserve,this.usedReserve,transactionType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return payment == that.payment && payout == that.payout && reserve == that.reserve && usedReserve == that.usedReserve && Objects.equals(id, that.id) && Objects.equals(accountId, that.accountId) && Objects.equals(orderId, that.orderId) && Objects.equals(userId, that.userId) && Objects.equals(currencyId, that.currencyId) && Objects.equals(text, that.text) && transactionType == that.transactionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, orderId, userId, currencyId, text, payment, payout, reserve, usedReserve, transactionType);
    }
}
