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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 13)
    private Long JMBG;
    @Column(nullable = false)
    private String position;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private Boolean active;

    private Permissions permissions;
}
