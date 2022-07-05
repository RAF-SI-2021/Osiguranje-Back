package dto.otc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BankAccountDTO {
    private Long id;
    private String accountNumber;
    private String bankName;
    private String accountType;
}
