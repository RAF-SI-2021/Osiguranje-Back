package raf.osiguranje.accounttransaction.model;

import raf.osiguranje.accounttransaction.model.dto.OrderDto;
import raf.osiguranje.accounttransaction.model.dto.SecurityType;
import raf.osiguranje.accounttransaction.model.dto.TransactionDTO;
import raf.osiguranje.accounttransaction.model.dto.TransactionType;

import javax.persistence.*;

@Entity
public class Transaction extends TransactionBase {

    @Column
    private int payment;

    @Column
    private int payout;

    @Column
    private int reserve;

    @Column
    private int usedReserve;

    public Transaction(){
    }

    public Transaction(Long accountId, Long orderId, Long userId, Long currencyId, Long securityId, SecurityType securityType, String text, TransactionType transactionType, int payment, int payout, int reserve, int usedReserve) {
        super(accountId, orderId, userId, currencyId, securityId, securityType, text, transactionType);
        this.payment = payment;
        this.payout = payout;
        this.reserve = reserve;
        this.usedReserve = usedReserve;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getPayout() {
        return payout;
    }

    public void setPayout(int payout) {
        this.payout = payout;
    }

    public int getReserve() {
        return reserve;
    }

    public void setReserve(int reserve) {
        this.reserve = reserve;
    }

    public int getUsedReserve() {
        return usedReserve;
    }

    public void setUsedReserve(int usedReserve) {
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
        return new TransactionDTO(this.id,this.accountId,this.timestamp,ord,this.userId,this.currencyId,this.securityId,this.securityType,
                this.text,this.payment,this.payout,this.reserve,this.usedReserve,transactionType);
    }

}
