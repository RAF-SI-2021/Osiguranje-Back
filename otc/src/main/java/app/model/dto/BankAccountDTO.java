package app.model.dto;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
    @NotNull
    private Long bankAccountID;
    private CompanyDTO company;
    @NotNull
    private String name;
    @NotNull
    private String accountType;
}
