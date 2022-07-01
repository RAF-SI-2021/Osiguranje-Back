package app.model.dto;

import app.model.Employee;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String companyPosition;
    private String description;

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.surname = employee.getSurname();
        this.phone = employee.getPhone();
        this.email = employee.getPhone();
        this.companyPosition = employee.getCompanyPosition();
        this.description = employee.getDescription();
    }
}
