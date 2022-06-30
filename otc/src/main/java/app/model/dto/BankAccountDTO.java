package app.model.dto;

import app.model.BankAccount;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
    private Long bankAccountID;
    private String bankName;
    private String accountType;

    public BankAccountDTO(BankAccount bankAccount) {
        this.bankAccountID = bankAccount.getBankAccountID();
        this.bankName = bankAccount.getBankName();
        this.accountType = bankAccount.getAccountType();
    }
}
