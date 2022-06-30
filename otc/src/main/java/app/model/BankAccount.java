package app.model;

import app.model.dto.BankAccountDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    @Id
    private Long bankAccountID;
    @ManyToOne
    private Company company;
    @Column
    private String bankName;
    @Column
    private String accountType;

    public BankAccount(BankAccountDTO bankAccountDTO) {
        this.bankName = bankAccountDTO.getBankName();
        this.accountType = bankAccountDTO.getAccountType();
    }
}
