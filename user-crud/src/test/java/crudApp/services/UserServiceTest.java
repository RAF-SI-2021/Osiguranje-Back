package crudApp.services;

import crudApp.dto.PasswordDto;
import crudApp.dto.UserCreateDto;
import crudApp.dto.UserDto;
import crudApp.mappers.PermissionMapper;
import crudApp.mappers.UserMapper;
import crudApp.model.PermissionAuthority;
import crudApp.model.Permissions;
import crudApp.model.User;
import crudApp.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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
    @Mock
    private ThreadPoolTaskExecutor taskExecutor;
    @Mock
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

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        userRepository.flush();
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
    @Disabled
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
        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());
        User capturedUser = argument.getValue();
        assertThat(capturedUser).isEqualTo(userCreateDto);
    }

    @Test
    void testCreateUserThrowsException() {
        User user = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user.setId(1L);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByEmail(any())).thenReturn(ofResult);
        assertThrows(Exception.class, () -> testUserService.createUser(new UserCreateDto()));
        verify(userRepository).findUserByEmail(any());
    }

    @Test
    void testCreateUser() throws Exception {
        User user1 = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user1.setId(1L);
        when(userRepository.save(any())).thenReturn(user1);
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());
        UserDto userDto = new UserDto();
        when(userMapper.userToUserDto(any())).thenReturn(userDto);
        when(userMapper.userCreateDtoToUser(any())).thenReturn(user1);
        assertSame(userDto, testUserService.createUser(new UserCreateDto()));
        verify(userRepository).save(any());
        verify(userRepository).findUserByEmail(any());
        verify(userMapper).userToUserDto(any());
        verify(userMapper).userCreateDtoToUser(any());
    }

    @Test
    void testUpdateUser() {
        User user1 = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user1.setId(1L);
        Optional<User> ofResult = Optional.of(user1);
        User user2 = new User("Radoje",
                "Milosevic",
                "rmilosevic@raf.rs",
                "1231231231235",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user2.setId(1L);
        when(userRepository.save(any())).thenReturn(user2);
        when(userRepository.findUserByEmail(any())).thenReturn(ofResult);
        User user3 = new User("Radenko",
                "Radenkovic",
                "rradenkovic@raf.rs",
                "1231231231236",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user2.setId(1L);
        when(userMapper.userDtoToUser(any())).thenReturn(user3);
        UserDto userDto = new UserDto();
        assertSame(userDto, testUserService.updateUser(userDto));
        verify(userRepository).save(any());
        verify(userRepository).findUserByEmail(any());
        verify(userMapper).userDtoToUser(any());
    }

    @Test
    void testUpdateUser2() {
        User user1 = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user1.setId(1L);
        Optional<User> ofResult = Optional.of(user1);
        User user2 = new User("Radoje",
                "Milosevic",
                "rmilosevic@raf.rs",
                "1231231231235",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user2.setId(1L);
        when(userRepository.findUserByEmail(any())).thenReturn(ofResult);
        when(userMapper.userDtoToUser(any())).thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class, () -> testUserService.updateUser(new UserDto()));
        verify(userRepository).findUserByEmail(any());
        verify(userMapper).userDtoToUser(any());
    }

    @Test
    void testUpdateUser3() {
        User user1 = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user1.setId(1L);
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());
        User user2 = new User("Radoje",
                "Milosevic",
                "rmilosevic@raf.rs",
                "1231231231235",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user2.setId(1L);
        assertThrows(UsernameNotFoundException.class, () -> testUserService.updateUser(new UserDto()));
        verify(userRepository).findUserByEmail(any());
    }

    @Test
    void testSetPassword() {
        User user1 = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user1.setId(1L);
        Optional<User> ofResult = Optional.of(user1);
        User user2 = new User("Radoje",
                "Milosevic",
                "rmilosevic@raf.rs",
                "1231231231235",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user2.setId(1L);
        when(userRepository.save(any())).thenReturn(user2);
        when(userRepository.findById(any())).thenReturn(ofResult);
        when(passwordEncoder.encode(any())).thenReturn("secret");
        PasswordDto passwordDto = new PasswordDto();
        this.testUserService.setPassword(passwordDto);
        verify(userRepository).save(any());
        verify(userRepository).findById(any());
        verify(passwordEncoder, atLeast(1)).encode(any());
        assertEquals("secret", passwordDto.getCurrentPassword());
        assertEquals("secret", passwordDto.getNewPassword());
    }

    @Test
    void testSetPassword2() {
        User user1 = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user1.setId(1L);
        Optional<User> ofResult = Optional.of(user1);
        User user2 = new User("Radoje",
                "Milosevic",
                "rmilosevic@raf.rs",
                "1231231231235",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user2.setId(1L);
        when(userRepository.findById(any())).thenReturn(ofResult);
        when(passwordEncoder.encode(any())).thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class, () -> this.testUserService.setPassword(new PasswordDto()));
        verify(userRepository).findById(any());
        verify(passwordEncoder).encode(any());
    }

    @Test
    void testSetPassword3() {
        User user1 = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user1.setId(1L);
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        User user2 = mock(User.class);
        doNothing().when(user2).setActive(any());
        doNothing().when(user2).setEmail(any());
        doNothing().when(user2).setFirstName(any());
        doNothing().when(user2).setId(any());
        doNothing().when(user2).setJMBG(any());
        doNothing().when(user2).setLastName(any());
        doNothing().when(user2).setPassword(any());
        doNothing().when(user2).setPermissions(any());
        doNothing().when(user2).setPhoneNumber(any());
        doNothing().when(user2).setPosition(any());
        user2.setActive(true);
        user2.setEmail("rmilosevic@raf.rs");
        user2.setFirstName("Radoje");
        user2.setId(123L);
        user2.setJMBG("1231231231235");
        user2.setLastName("Milosevic");
        user2.setPassword("radenko");
        user2.setPermissions(new Permissions());
        user2.setPhoneNumber("4105551212");
        user2.setPosition("RAF");
        assertThrows(UsernameNotFoundException.class, () -> testUserService.setPassword(new PasswordDto()));
        verify(userRepository).findById(any());
        verify(user2).setActive(any());
        verify(user2).setEmail(any());
        verify(user2).setFirstName(any());
        verify(user2).setId(any());
        verify(user2).setJMBG(any());
        verify(user2).setLastName(any());
        verify(user2).setPassword(any());
        verify(user2).setPermissions(any());
        verify(user2).setPhoneNumber(any());
        verify(user2).setPosition(any());
    }

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user.setId(1L);
        user.setPassword("radenko");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByEmail(any())).thenReturn(ofResult);
        when(permissionMapper.permissionsToPermissionAuthority(any()))
                .thenReturn(new PermissionAuthority(new Permissions()));
        UserDetails actualLoadUserByUsernameResult = testUserService.loadUserByUsername("mradenkovic@raf.rs");
        assertEquals(1, actualLoadUserByUsernameResult.getAuthorities().size());
        assertTrue(actualLoadUserByUsernameResult.isEnabled());
        assertTrue(actualLoadUserByUsernameResult.isCredentialsNonExpired());
        assertTrue(actualLoadUserByUsernameResult.isAccountNonLocked());
        assertTrue(actualLoadUserByUsernameResult.isAccountNonExpired());
        assertEquals("mradenkovic@raf.rs", actualLoadUserByUsernameResult.getUsername());
        assertEquals("radenko", actualLoadUserByUsernameResult.getPassword());
        verify(userRepository).findUserByEmail(any());
        verify(permissionMapper).permissionsToPermissionAuthority(any());
    }

    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        User user = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user.setId(1L);
        user.setPassword("radenko");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByEmail(any())).thenReturn(ofResult);
        when(permissionMapper.permissionsToPermissionAuthority(any()))
                .thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class, () -> this.testUserService.loadUserByUsername("mradenkovic@raf.rs"));
        verify(userRepository).findUserByEmail(any());
        verify(permissionMapper).permissionsToPermissionAuthority(any());
    }

    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());

        User user = mock(User.class);
        doNothing().when(user).setActive(any());
        doNothing().when(user).setEmail(any());
        doNothing().when(user).setFirstName(any());
        doNothing().when(user).setId(any());
        doNothing().when(user).setJMBG(any());
        doNothing().when(user).setLastName(any());
        doNothing().when(user).setPassword(any());
        doNothing().when(user).setPermissions(any());
        doNothing().when(user).setPhoneNumber(any());
        doNothing().when(user).setPosition(any());
        user.setActive(true);
        user.setEmail("mradenkovic@raf.rs");
        user.setFirstName("Milos");
        user.setId(1L);
        user.setJMBG("1231231231234");
        user.setLastName("Radenkovic");
        user.setPassword("radenko");
        user.setPermissions(new Permissions());
        user.setPhoneNumber("4105551212");
        user.setPosition("RAF");
        assertThrows(UsernameNotFoundException.class, () -> testUserService.loadUserByUsername("mradenkovic@raf.rs"));
        verify(userRepository).findUserByEmail(any());
        verify(user).setActive(any());
        verify(user).setEmail(any());
        verify(user).setFirstName(any());
        verify(user).setId(any());
        verify(user).setJMBG(any());
        verify(user).setLastName(any());
        verify(user).setPassword(any());
        verify(user).setPermissions(any());
        verify(user).setPhoneNumber(any());
        verify(user).setPosition(any());
    }
}