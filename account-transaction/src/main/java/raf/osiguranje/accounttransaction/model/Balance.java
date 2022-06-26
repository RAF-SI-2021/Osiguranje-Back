package raf.osiguranje.accounttransaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.osiguranje.accounttransaction.model.dto.SecurityType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(BalanceId.class)
public class Balance {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_account_number", nullable = false)
    private Account account;

    @Id
    private Long securityId;

    @Column
    private SecurityType securityType;

    @Column
    private Integer amount;

    @Column
    private Integer reserved;

    public Balance(Account account, Long securityId, SecurityType securityType, Integer amount) {
        this.account = account;
        this.securityId = securityId;
        this.securityType = securityType;
        this.amount = amount;
    }

    public Integer getAvailable(){
        return this.amount - this.reserved;
    }

    public BalanceId getBalanceId(){
        return new BalanceId(account,securityId);
    }
}
