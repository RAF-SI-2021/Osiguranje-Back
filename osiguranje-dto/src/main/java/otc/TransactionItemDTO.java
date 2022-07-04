package otc;

import accounts.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import securities.SecurityType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionItemDTO {

    private Long id;
    private TransactionType transactionType;
    private Long securityId;
    private SecurityType securityType;
    private Long accountId;
    private Long currencyId;
    private int amount;
    private double pricePerShare;

}
