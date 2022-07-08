package app.model.api;


import app.model.TransactionType;
import lombok.*;

@AllArgsConstructor
@Getter
public class TransactionOtcDto {
    private TransactionType transactionType;
    private Long accountId;
    private Long securityId;
    private SecurityType securityType;
    private Long userId;
    private Long currencyId;
    private int payment;
    private int payout;
    private int reserve;
    private int usedReserve;
    private String text = "";

    public TransactionOtcDto(TransactionType transactionType, Long accountId, Long securityId, SecurityType securityType, Long userId, Long currencyId, int payment, int payout, int reserve, int usedReserve) {
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.securityId = securityId;
        this.securityType = securityType;
        this.userId = userId;
        this.currencyId = currencyId;
        this.payment = payment;
        this.payout = payout;
        this.reserve = reserve;
        this.usedReserve = usedReserve;
    }
}
