package app.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "employee")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long employeeId;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String telNumber;
    @Column
    private String email;
    @Column
    private String companyPosition;
    @Column
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_ID", nullable = false)
    private Company company;
}
