package crudApp.mappers;

import crudApp.model.User;
import crudApp.model.UserDto;

public class UserMapper {
    public UserDto userToUserDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPosition(), user.getPhoneNumber(), user.getActive(), user.getPermissions());
    }
}
