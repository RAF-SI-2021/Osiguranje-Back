package raf.osiguranje.accounttransaction.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BalanceDTO {

    private Long accountId;

    private Long securityId;
    private SecurityType securityType;

    private int amount;

    /*
    Ove dve promenljive se ne koriste pri kreiranju objekta u sistemu,
    ali se koriste kada se vraca na frontend
     */
    private int reserved;
    private int available;

}
