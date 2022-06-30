package app.model.dto;

import app.model.BankAccount;
import app.model.Company;
import app.model.Employee;
import lombok.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    private Long ID;
    private Long registrationID;
    private String name;
    private Long taxID;
    private Long industrialClassificationID;
    private String address;
    private List<EmployeeDTO> employees;
    private List<BankAccountDTO> bankAccounts;

    public CompanyDTO(Company company) {
        this.ID = company.getID();
        this.registrationID = company.getRegistrationID();
        this.name = company.getName();
        this.taxID = company.getTaxID();
        this.industrialClassificationID = company.getIndustrialClassificationID();
        this.address = company.getAddress();
        employees = new ArrayList<>();
        bankAccounts = new ArrayList<>();

        for(Employee employee : company.getEmployees()){
            employees.add(new EmployeeDTO(employee));
        }

        for(BankAccount bankAccount : company.getBankAccounts()){
            bankAccounts.add(new BankAccountDTO(bankAccount));
        }
    }
}
