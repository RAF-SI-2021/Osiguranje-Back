package raf.osiguranje.accounttransaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.osiguranje.accounttransaction.model.dto.SecurityType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceId implements Serializable {

    protected Long accountId;

    protected Long securityId;

    private SecurityType securityType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceId balanceId = (BalanceId) o;
        return Objects.equals(accountId, balanceId.accountId) && Objects.equals(securityId, balanceId.securityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, securityId,securityType);
    }

    @Override
    public String toString() {
        return "BalanceId{" +
                "accountId=" + accountId +
                ", securityId=" + securityId +
                ", securityType=" + securityType +
                '}';
    }
}
