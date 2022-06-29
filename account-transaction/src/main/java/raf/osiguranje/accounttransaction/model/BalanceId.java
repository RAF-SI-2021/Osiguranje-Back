package raf.osiguranje.accounttransaction.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class BalanceId implements Serializable {

    protected Long accountId;

    protected Long securityId;
}
