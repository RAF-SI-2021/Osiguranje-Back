package gateway.user;

import lombok.Getter;

@Getter
public class UserInfo {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private String phoneNumber;
    private Boolean active;
    private Permissions permissions;

    public UserInfo(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.position = user.getPosition();
        this.phoneNumber = user.getPhoneNumber();
        this.active = user.getActive();
        this.permissions = user.getPermissions();
    }
}
