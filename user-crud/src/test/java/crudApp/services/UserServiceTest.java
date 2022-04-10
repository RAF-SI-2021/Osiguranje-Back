package crudApp.services;

import crudApp.dto.UserCreateDto;
import crudApp.dto.UserDto;
import crudApp.mappers.PermissionMapper;
import crudApp.mappers.UserMapper;
import crudApp.model.Permissions;
import crudApp.model.User;
import crudApp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PermissionMapper permissionMapper;
    private User user1;
    private User user2;
    private UserDto userDto1;
    private UserDto userDto2;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private UserService testUserService;

    @BeforeEach
    void setUp() {
        this.user1 = Mockito.mock(User.class);
        this.user2 = Mockito.mock(User.class);
        this.userDto1 = Mockito.mock(UserDto.class);
        this.userDto2 = Mockito.mock(UserDto.class);
        testUserService = new UserService(userRepository, userMapper, permissionMapper, taskExecutor, passwordEncoder);
    }

    @Test
    void canFindAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        when(userMapper.userToUserDto(user2)).thenReturn(userDto2);
        testUserService.findAll();
        verify(userRepository).findAll();
        verify(userMapper).userToUserDto(user1);
        verify(userMapper).userToUserDto(user2);
    }

    @Test
    void canFindUserByEmail() {
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.ofNullable(user1));
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        testUserService.findUserByEmail(Mockito.anyString());
        verify(userRepository).findUserByEmail(Mockito.anyString());
        verify(userMapper).userToUserDto(user1);
    }

    @Test
    void canFindUsersByFirstName() {
        when(userRepository.findUsersByFirstName(Mockito.anyString())).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        when(userMapper.userToUserDto(user2)).thenReturn(userDto2);
        testUserService.findUsersByFirstName(Mockito.anyString());
        verify(userRepository).findUsersByFirstName(Mockito.anyString());
        verify(userMapper).userToUserDto(user1);
        verify(userMapper).userToUserDto(user2);
    }

    @Test
    void findUsersByLastName() {
        when(userRepository.findUsersByLastName(Mockito.anyString())).thenReturn(List.of(user1));
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        testUserService.findUsersByLastName(Mockito.anyString());
        verify(userRepository).findUsersByLastName(Mockito.anyString());
        verify(userMapper).userToUserDto(user1);
    }

    @Test
    void findUsersByPosition() {
        when(userRepository.findUsersByPosition(Mockito.anyString())).thenReturn(List.of(user2));
        when(userMapper.userToUserDto(user2)).thenReturn(userDto2);
        testUserService.findUsersByPosition(Mockito.anyString());
        verify(userRepository).findUsersByPosition(Mockito.anyString());
        verify(userMapper).userToUserDto(user2);
    }

    @Test
    void canCreateUser() throws Exception {
        //given
        UserCreateDto userCreateDto = new UserCreateDto("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        //when
        testUserService.createUser(userCreateDto);
        //then
        ArgumentCaptor<User> userCreateDtoArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCreateDtoArgumentCaptor.capture());
        User capturedUser = userCreateDtoArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(userCreateDto);
    }

    @Test
    void updateUser() {
    }

    @Test
    void setPassword() {
    }

    @Test
    void loadUserByUsername() {
    }
}