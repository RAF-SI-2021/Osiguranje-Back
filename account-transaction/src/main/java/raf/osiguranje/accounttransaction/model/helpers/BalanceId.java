package raf.osiguranje.accounttransaction.model.helpers;

import lombok.*;
import raf.osiguranje.accounttransaction.model.Account;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BalanceId implements Serializable {

    private Account account;
    private Long securityId;

}
