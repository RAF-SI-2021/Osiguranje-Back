package crudApp.model;

import javax.persistence.*;

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
    @Column
    private String password;
    @Column(unique = true, nullable = false, length = 13)
    private String JMBG;
    @Column(nullable = false)
    private String position;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private Boolean active;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "admin", column = @Column(name = "admin")),
            @AttributeOverride(name = "stockTrading", column = @Column(name = "stock_trading")),
            @AttributeOverride(name = "stockOverview", column = @Column(name = "stock_overview")),
            @AttributeOverride(name = "contractConclusion", column = @Column(name = "contract_conclusion")),
            @AttributeOverride(name = "supervisor", column = @Column(name = "supervisor")),
            @AttributeOverride(name = "traineeAgent", column = @Column(name = "trainee_agent")),
            @AttributeOverride(name = "agent", column = @Column(name = "agent"))
    })
    private Permissions permissions;

    public User() {}

    public User(String firstName, String lastName, String email, String JMBG, String position, String phoneNumber, Boolean active, Permissions permissions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.JMBG = JMBG;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.permissions = permissions;
    }

    public User(Long id, String firstName, String lastName, String email, String position, String phoneNumber, Boolean active, Permissions permissions) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJMBG() {
        return JMBG;
    }

    public void setJMBG(String JMBG) {
        this.JMBG = JMBG;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }
}
