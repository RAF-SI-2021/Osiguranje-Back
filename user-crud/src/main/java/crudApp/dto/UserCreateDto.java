package crudApp.dto;

import crudApp.model.Permissions;

public class UserCreateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String JMBG;
    private String position;
    private String phoneNumber;
    private Boolean active;
    private Permissions permissions;

    public UserCreateDto() {}

    public UserCreateDto(String firstName, String lastName, String email, String JMBG, String position, String phoneNumber, Boolean active, Permissions permissions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.JMBG = JMBG;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.permissions = permissions;
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
