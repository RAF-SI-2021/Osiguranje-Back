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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String accountNumber;
    @ManyToOne
    private Company company;
    @Column
    private String bankName;
    @Column
    private String accountType;

    public BankAccount(BankAccountDTO bankAccountDTO) {
        this.accountNumber = bankAccountDTO.getAccountNumber();
        this.bankName = bankAccountDTO.getBankName();
        this.accountType = bankAccountDTO.getAccountType();
    }

    public BankAccount(String accontNumber, String bankName, String accountType) {
        this.accontNumber = accontNumber;
        this.bankName = bankName;
        this.accountType = accountType;
    }
}
