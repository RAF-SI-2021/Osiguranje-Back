package app.model.api;


import app.model.TransactionType;
import lombok.*;

@AllArgsConstructor
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

}
