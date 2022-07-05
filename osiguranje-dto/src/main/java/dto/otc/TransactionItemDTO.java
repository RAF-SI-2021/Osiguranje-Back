package dto.otc;

import dto.accounts.TransactionType;
import dto.securities.SecurityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
