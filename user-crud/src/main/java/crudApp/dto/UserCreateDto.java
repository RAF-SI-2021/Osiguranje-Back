package crudApp.dto;

import crudApp.model.Permissions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String JMBG;
    private String position;
    private String phoneNumber;
    private Boolean active;
    private Permissions permissions;

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
}
