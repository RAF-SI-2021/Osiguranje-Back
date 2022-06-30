package app.model;

import app.model.dto.CompanyDTO;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    @Column
    private Long registrationID;
    @Column
    private String name;
    @Column
    private Long taxID;
    @Column
    private Long industrialClassificationID;
    @Column
    private String address;
    @OneToMany(mappedBy = "employeeID", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Employee> employees;
    @OneToMany(mappedBy = "contractID", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contract> contracts;

    public Company(CompanyDTO companyDTO) {
        this.registrationID = companyDTO.getRegistrationID();
        this.name = companyDTO.getName();
        this.taxID = companyDTO.getTaxID();
        this.industrialClassificationID = companyDTO.getIndustrialClassificationID();
        this.address = companyDTO.getAddress();
        this.employees = new ArrayList<>();
        this.contracts = new ArrayList<>();
    }
}
