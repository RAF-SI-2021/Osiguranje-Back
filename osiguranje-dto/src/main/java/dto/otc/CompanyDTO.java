package dto.otc;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    private Long id;
    private Long registrationID;
    private String name;
    private Long taxID;
    private Long industrialClassificationID;
    private String address;
    private List<EmployeeDTO> employees;
    private List<BankAccountDTO> bankAccounts;
}
