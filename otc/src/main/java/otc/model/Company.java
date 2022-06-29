package otc.model;

import lombok.*;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "company")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
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
    private Set<Employee> employees;
}
