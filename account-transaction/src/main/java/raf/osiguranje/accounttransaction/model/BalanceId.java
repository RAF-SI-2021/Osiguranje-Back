package raf.osiguranje.accounttransaction.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BalanceId implements Serializable {

    private Account account;
    private Long securityId;

}
