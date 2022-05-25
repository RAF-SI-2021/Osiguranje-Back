package buyingmarket.model.dto;

import buyingmarket.model.Permissions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private String phoneNumber;
    private Boolean active;
    private Permissions permissions;

    public UserDto(Long id, String firstName, String lastName, String email, String position, String phoneNumber, Boolean active, Permissions permissions){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.permissions = permissions;
    }

    public UserDto(String firstName, String lastName, String email, String position, String phoneNumber, Boolean active, Permissions permissions){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.permissions = permissions;
    }
}

