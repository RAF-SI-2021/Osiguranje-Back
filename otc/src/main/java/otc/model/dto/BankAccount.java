package otc.model.dto;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    @NotNull
    private Long bankAccountID;
    private Company owner;
    @NotNull
    private String name;
    @NotNull
    private String accountType;
}
