package gateway.user;

import lombok.Getter;

@Getter
public class UserInfo {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String position;
    private final String phoneNumber;
    private final Boolean active;
    private final Permissions permissions;

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
