package app.model.api;

import app.model.Action;
import lombok.*;

@Getter
@AllArgsConstructor
public class TransactionOtcDto {
    private Action action;
    private Long accountId;
    private Long securityId;
    private SecurityType securityType;
    private Long userId;
    private Long currencyId;
    private double payment;
    private double payout;
    private double reserve;
    private double usedReserve;
}
