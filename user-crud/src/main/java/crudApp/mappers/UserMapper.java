package crudApp.mappers;

import crudApp.model.User;
import crudApp.model.UserCreateDto;
import crudApp.model.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToUserDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPosition(), user.getPhoneNumber(), user.getActive(), user.getPermissions());
    }

    public User userDtoToUser(UserDto dto) {
        return new User(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getPosition(), dto.getPhoneNumber(), dto.getActive(), dto.getPermissions());
    }

    public User userCreateDtoToUser(UserCreateDto dto) {
        return new User(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getJMBG(), dto.getPosition(), dto.getPhoneNumber(), dto.getActive(), dto.getPermissions());
    }
}
