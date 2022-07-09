package raf.osiguranje.accounttransaction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateDto {

    private Long accountId;
    private Long securityId;
    private SecurityType securityType;
    private int amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceUpdateDto that = (BalanceUpdateDto) o;
        return amount == that.amount && Objects.equals(accountId, that.accountId) && Objects.equals(securityId, that.securityId) && securityType == that.securityType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, securityId, securityType, amount);
    }
}
