package raf.osiguranje.accounttransaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(BalanceId.class)
public class AccountBalance {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_account_number", nullable = false)
    private Account account;

    @Id
    private Long securityId;

    @Column
    private Integer amount;

    @Column
    private Integer reserved;

    public Integer getAvailable(){
        return this.amount - this.reserved;
    }
}
