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
    @Column(nullable = false)
    private String JMBG;
    @Column(nullable = false)
    private String pozicija;
    @Column(nullable = false)
    private String brojTelefona;
    @Column(nullable = false)
    private Boolean aktivan;

    private Permissions permissions;
}
