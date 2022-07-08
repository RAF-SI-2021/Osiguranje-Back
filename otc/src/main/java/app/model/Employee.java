package app.model;

import app.model.dto.EmployeeDTO;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private String companyPosition;
    @Column
    private String description;
    @ManyToOne
    private Company company;

    public Employee(EmployeeDTO employeeDTO) {
        this.name = employeeDTO.getName();
        this.surname = employeeDTO.getSurname();
        this.phone = employeeDTO.getPhone();
        this.email = employeeDTO.getEmail();
        this.companyPosition = employeeDTO.getCompanyPosition();
        this.description = employeeDTO.getDescription();
    }

    public Employee(String name, String surname, String phone, String email, String companyPosition, String description) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.companyPosition = companyPosition;
        this.description = description;
    }
}
