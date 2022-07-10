package raf.osiguranje.accounttransaction.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BalanceUpdateDto {

    private Long accountId;
    private Long securityId;
    private SecurityType securityType;
    private int amount;
}
