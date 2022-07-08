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
    private String accontNumber;
    @ManyToOne
    private Company company;
    @Column
    private String bankName;
    @Column
    private String accountType;

    public BankAccount(BankAccountDTO bankAccountDTO) {
        this.accontNumber = bankAccountDTO.getAccountNumber();
        this.bankName = bankAccountDTO.getBankName();
        this.accountType = bankAccountDTO.getAccountType();
    }
}
