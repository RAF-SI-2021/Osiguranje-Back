package crudApp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //consider changing to GenerationType.AUTO
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false, length = 13)
    private Long JMBG;
    @Column(nullable = false)
    private String position;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private Boolean active;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "admin", column = @Column(name = "admin")),
            @AttributeOverride(name = "stockTrading", column = @Column(name = "stock_trading")),
            @AttributeOverride(name = "stockOverview", column = @Column(name = "stock_overview")),
            @AttributeOverride(name = "contractConclusion", column = @Column(name = "contract_conclusion"))
    })
    private Permissions permissions;
}
