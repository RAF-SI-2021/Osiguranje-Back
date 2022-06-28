package otc.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "employee")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    @Id
    private Long bankAccountID;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bankAccount_ID", nullable = false)
    private Company owner;
    @Column
    private String name;
    @Column
    private String accountType;
}
