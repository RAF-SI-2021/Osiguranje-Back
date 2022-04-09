package crudApp.mappers;

import crudApp.dto.UserCreateDto;
import crudApp.dto.UserDto;
import crudApp.model.Permissions;
import crudApp.model.User;
import crudApp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {
    @Autowired
    private UserMapper testUserMapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    void userToUserDtoTest() {
        //given
        User user = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        //when
        user = userRepository.save(user);
        UserDto userDto = testUserMapper.userToUserDto(user);
        //then
        assertThat(userDto != null).isTrue();
        assertThat(userDto.getId().equals(user.getId())).isTrue();
        assertThat(userDto.getFirstName().equals(user.getFirstName())).isTrue();
        assertThat(userDto.getLastName().equals(user.getLastName())).isTrue();
        assertThat(userDto.getEmail().equals(user.getEmail())).isTrue();
        assertThat(userDto.getPosition().equals(user.getPosition())).isTrue();
        assertThat(userDto.getPhoneNumber().equals(user.getPhoneNumber())).isTrue();
        assertThat(userDto.getActive().equals(user.getActive())).isTrue();
        assertThat(userDto.getPermissions().equals(user.getPermissions())).isTrue();
    }

    @Test
    void userDtoToUserTest() {
        //given
        UserDto userDto = new UserDto(1L,
                "Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        //when
        User user = testUserMapper.userDtoToUser(userDto);
        //then
        assertThat(user != null).isTrue();
        assertThat(user.getId().equals(userDto.getId())).isTrue();
        assertThat(user.getFirstName().equals(userDto.getFirstName())).isTrue();
        assertThat(user.getLastName().equals(userDto.getLastName())).isTrue();
        assertThat(user.getEmail().equals(userDto.getEmail())).isTrue();
        assertThat(user.getPosition().equals(userDto.getPosition())).isTrue();
        assertThat(user.getPhoneNumber().equals(userDto.getPhoneNumber())).isTrue();
        assertThat(user.getActive().equals(userDto.getActive())).isTrue();
        assertThat(user.getPermissions().equals(userDto.getPermissions())).isTrue();
    }

    @Test
    void userCreateDtoToUserTest() {
        //given
        UserCreateDto userCreateDto = new UserCreateDto("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1234123412345",
                "RAF",
                "0651231234",
                true,
                new Permissions());
        //when
        User user = testUserMapper.userCreateDtoToUser(userCreateDto);
        //then
        assertThat(user != null).isTrue();
        assertThat(user.getFirstName().equals(userCreateDto.getFirstName())).isTrue();
        assertThat(user.getLastName().equals(userCreateDto.getLastName())).isTrue();
        assertThat(user.getEmail().equals(userCreateDto.getEmail())).isTrue();
        assertThat(user.getJMBG().equals(userCreateDto.getJMBG())).isTrue();
        assertThat(user.getPosition().equals(userCreateDto.getPosition())).isTrue();
        assertThat(user.getPhoneNumber().equals(userCreateDto.getPhoneNumber())).isTrue();
        assertThat(user.getActive().equals(userCreateDto.getActive())).isTrue();
        assertThat(user.getPermissions().equals(userCreateDto.getPermissions())).isTrue();
    }
}