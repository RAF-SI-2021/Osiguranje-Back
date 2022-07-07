package app.model.dto;

import app.model.BankAccount;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BankAccountDTO {
    private Long id;
    private String accountNumber;
    private String bankName;
    private String accountType;

    public BankAccountDTO(BankAccount bankAccount) {
        this.id = bankAccount.getId();
        this.accountNumber = bankAccount.getAccontNumber();
        this.bankName = bankAccount.getBankName();
        this.accountType = bankAccount.getAccountType();
    }
}
