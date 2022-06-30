package raf.osiguranje.accounttransaction.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class BalanceId implements Serializable {

    protected Long accountId;

    protected Long securityId;

    public BalanceId(Long accountId, Long securityId) {
        this.accountId = accountId;
        this.securityId = securityId;
    }

    public BalanceId() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getSecurityId() {
        return securityId;
    }

    public void setSecurityId(Long securityId) {
        this.securityId = securityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceId balanceId = (BalanceId) o;
        return Objects.equals(accountId, balanceId.accountId) && Objects.equals(securityId, balanceId.securityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, securityId);
    }

    @Override
    public String toString() {
        return "BalanceId{" +
                "accountId=" + accountId +
                ", securityId=" + securityId +
                '}';
    }
}
