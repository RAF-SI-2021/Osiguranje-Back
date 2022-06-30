package raf.osiguranje.accounttransaction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateDto {

    private Long accountId;
    private Long securityId;
    private SecurityType securityType;
    private int amount;

}
