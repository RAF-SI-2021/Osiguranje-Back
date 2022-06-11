package crudApp.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import crudApp.dto.PasswordDto;
import crudApp.dto.UserCreateDto;
import crudApp.dto.UserDto;
import crudApp.mappers.PermissionMapper;
import crudApp.mappers.UserMapper;
import crudApp.model.Permissions;
import crudApp.model.User;
import crudApp.repositories.UserRepository;
import crudApp.services.UserService;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserRestController.class})
@ExtendWith(SpringExtension.class)
class UserRestControllerTest {
    @Autowired
    private UserRestController userRestController;

    @MockBean
    private UserService userService;

    @Test
    void testGetAllUsers() {
        UserRepository userRepository = mock(UserRepository.class);
        ArrayList<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualAllUsers = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .getAllUsers();
        assertEquals(userList, actualAllUsers.getBody());
        assertEquals(HttpStatus.OK, actualAllUsers.getStatusCode());
        assertTrue(actualAllUsers.getHeaders().isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void testGetAllUsers2() {
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

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(userList);
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualAllUsers = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .getAllUsers();
        assertEquals(1, ((Collection<UserDto>) actualAllUsers.getBody()).size());
        assertTrue(actualAllUsers.hasBody());
        assertEquals(HttpStatus.OK, actualAllUsers.getStatusCode());
        assertTrue(actualAllUsers.getHeaders().isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void testGetAllUsers4() {
        UserService userService = mock(UserService.class);
        when(userService.findAll()).thenReturn(new ArrayList<>());
        ResponseEntity<?> actualAllUsers = (new UserRestController(userService)).getAllUsers();
        assertTrue(actualAllUsers.hasBody());
        assertEquals(HttpStatus.OK, actualAllUsers.getStatusCode());
        assertTrue(actualAllUsers.getHeaders().isEmpty());
        verify(userService).findAll();
    }

    @Test
    void testGetAllUsers6() {
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

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(userList);
        UserMapper userMapper = mock(UserMapper.class);
        when(userMapper.userToUserDto(any())).thenReturn(new UserDto());
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualAllUsers = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .getAllUsers();
        assertEquals(1, ((Collection<UserDto>) actualAllUsers.getBody()).size());
        assertTrue(actualAllUsers.hasBody());
        assertEquals(HttpStatus.OK, actualAllUsers.getStatusCode());
        assertTrue(actualAllUsers.getHeaders().isEmpty());
        verify(userRepository).findAll();
        verify(userMapper).userToUserDto(any());
    }

    @Test
    void testCreateUser() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setActive(true);
        userCreateDto.setEmail("mradenkovic@raf.rs");
        userCreateDto.setFirstName("Milos");
        userCreateDto.setJMBG("1231231231234");
        userCreateDto.setLastName("Radenkovic");
        userCreateDto.setPermissions(new Permissions());
        userCreateDto.setPhoneNumber("4105551212");
        userCreateDto.setPosition("RAF");
        String content = (new ObjectMapper()).writeValueAsString(userCreateDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userRestController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    @Test
    void testFindUserByEmail() {
        Permissions permissions = new Permissions();
        User user = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                permissions);
        user.setId(1L);
        user.setPassword("radenko");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(user));
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualFindUserByEmailResult = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .findUserByEmail("foo");
        assertTrue(actualFindUserByEmailResult.hasBody());
        assertTrue(actualFindUserByEmailResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualFindUserByEmailResult.getStatusCode());
        assertSame(permissions, ((UserDto) actualFindUserByEmailResult.getBody()).getPermissions());
        assertEquals("Radenkovic", ((UserDto) actualFindUserByEmailResult.getBody()).getLastName());
        assertEquals("RAF", ((UserDto) actualFindUserByEmailResult.getBody()).getPosition());
        assertTrue(((UserDto) actualFindUserByEmailResult.getBody()).getActive());
        assertEquals("mradenkovic@raf.rs", ((UserDto) actualFindUserByEmailResult.getBody()).getEmail());
        assertEquals("0641231234", ((UserDto) actualFindUserByEmailResult.getBody()).getPhoneNumber());
        assertEquals(1L, ((UserDto) actualFindUserByEmailResult.getBody()).getId().longValue());
        assertEquals("Milos", ((UserDto) actualFindUserByEmailResult.getBody()).getFirstName());
        verify(userRepository).findUserByEmail(any());
    }

    @Test
    void testFindUserById() {
        Permissions permissions = new Permissions();
        permissions.setAdmin(true);
        User user = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                permissions);
        user.setId(2L);
        user.setPassword("radenko");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualFindUserByIdResult = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .findUserById(2L);
        assertTrue(actualFindUserByIdResult.hasBody());
        assertTrue(actualFindUserByIdResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualFindUserByIdResult.getStatusCode());
        assertSame(permissions, ((UserDto) actualFindUserByIdResult.getBody()).getPermissions());
        assertEquals("Radenkovic", ((UserDto) actualFindUserByIdResult.getBody()).getLastName());
        assertEquals("RAF", ((UserDto) actualFindUserByIdResult.getBody()).getPosition());
        assertTrue(((UserDto) actualFindUserByIdResult.getBody()).getActive());
        assertEquals("mradenkovic@raf.rs", ((UserDto) actualFindUserByIdResult.getBody()).getEmail());
        assertEquals("0641231234", ((UserDto) actualFindUserByIdResult.getBody()).getPhoneNumber());
        assertEquals(2L, ((UserDto) actualFindUserByIdResult.getBody()).getId().longValue());
        assertEquals("Milos", ((UserDto) actualFindUserByIdResult.getBody()).getFirstName());
        verify(userRepository).findById(any());
    }


    @Test
    void testFindUserByEmail2() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualFindUserByEmailResult = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .findUserByEmail("foo");
        assertNull(actualFindUserByEmailResult.getBody());
        assertEquals(HttpStatus.OK, actualFindUserByEmailResult.getStatusCode());
        assertTrue(actualFindUserByEmailResult.getHeaders().isEmpty());
        verify(userRepository).findUserByEmail(any());
    }

    @Test
    void testFindUserByEmail4() {
        UserService userService = mock(UserService.class);
        when(userService.findUserByEmail(any())).thenReturn(new UserDto());
        ResponseEntity<?> actualFindUserByEmailResult = (new UserRestController(userService)).findUserByEmail("foo");
        assertTrue(actualFindUserByEmailResult.hasBody());
        assertTrue(actualFindUserByEmailResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualFindUserByEmailResult.getStatusCode());
        verify(userService).findUserByEmail(any());
    }

    @Test
    void testFindUsersByFirstName() {
        UserRepository userRepository = mock(UserRepository.class);
        ArrayList<User> userList = new ArrayList<>();
        when(userRepository.findUsersByFirstName(any())).thenReturn(userList);
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualFindUsersByFirstNameResult = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .findUsersByFirstName("foo");
        assertEquals(userList, actualFindUsersByFirstNameResult.getBody());
        assertEquals(HttpStatus.OK, actualFindUsersByFirstNameResult.getStatusCode());
        assertTrue(actualFindUsersByFirstNameResult.getHeaders().isEmpty());
        verify(userRepository).findUsersByFirstName(any());
    }

    @Test
    void testFindUsersByFirstName2() {
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

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findUsersByFirstName(any())).thenReturn(userList);
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualFindUsersByFirstNameResult = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .findUsersByFirstName("foo");
        assertEquals(1, ((Collection<UserDto>) actualFindUsersByFirstNameResult.getBody()).size());
        assertTrue(actualFindUsersByFirstNameResult.hasBody());
        assertEquals(HttpStatus.OK, actualFindUsersByFirstNameResult.getStatusCode());
        assertTrue(actualFindUsersByFirstNameResult.getHeaders().isEmpty());
        verify(userRepository).findUsersByFirstName(any());
    }

    @Test
    void testFindUsersByFirstName4() {
        UserService userService = mock(UserService.class);
        when(userService.findUsersByFirstName(any())).thenReturn(new ArrayList<>());
        ResponseEntity<?> actualFindUsersByFirstNameResult = (new UserRestController(userService))
                .findUsersByFirstName("foo");
        assertTrue(actualFindUsersByFirstNameResult.hasBody());
        assertEquals(HttpStatus.OK, actualFindUsersByFirstNameResult.getStatusCode());
        assertTrue(actualFindUsersByFirstNameResult.getHeaders().isEmpty());
        verify(userService).findUsersByFirstName(any());
    }

    @Test
    void testFindUsersByLastName() {
        UserRepository userRepository = mock(UserRepository.class);
        ArrayList<User> userList = new ArrayList<>();
        when(userRepository.findUsersByLastName(any())).thenReturn(userList);
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualFindUsersByLastNameResult = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .findUsersByLastName("foo");
        assertEquals(userList, actualFindUsersByLastNameResult.getBody());
        assertEquals(HttpStatus.OK, actualFindUsersByLastNameResult.getStatusCode());
        assertTrue(actualFindUsersByLastNameResult.getHeaders().isEmpty());
        verify(userRepository).findUsersByLastName(any());
    }

    @Test
    void testFindUsersByLastName2() {
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

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findUsersByLastName(any())).thenReturn(userList);
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualFindUsersByLastNameResult = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .findUsersByLastName("foo");
        assertEquals(1, ((Collection<UserDto>) actualFindUsersByLastNameResult.getBody()).size());
        assertTrue(actualFindUsersByLastNameResult.hasBody());
        assertEquals(HttpStatus.OK, actualFindUsersByLastNameResult.getStatusCode());
        assertTrue(actualFindUsersByLastNameResult.getHeaders().isEmpty());
        verify(userRepository).findUsersByLastName(any());
    }

    @Test
    void testFindUsersByLastName4() {
        UserService userService = mock(UserService.class);
        when(userService.findUsersByLastName(any())).thenReturn(new ArrayList<>());
        ResponseEntity<?> actualFindUsersByLastNameResult = (new UserRestController(userService))
                .findUsersByLastName("foo");
        assertTrue(actualFindUsersByLastNameResult.hasBody());
        assertEquals(HttpStatus.OK, actualFindUsersByLastNameResult.getStatusCode());
        assertTrue(actualFindUsersByLastNameResult.getHeaders().isEmpty());
        verify(userService).findUsersByLastName(any());
    }

    @Test
    void testFindUsersByPosition() {
        UserRepository userRepository = mock(UserRepository.class);
        ArrayList<User> userList = new ArrayList<>();
        when(userRepository.findUsersByPosition(any())).thenReturn(userList);
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualFindUsersByPositionResult = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .findUsersByPosition("foo");
        assertEquals(userList, actualFindUsersByPositionResult.getBody());
        assertEquals(HttpStatus.OK, actualFindUsersByPositionResult.getStatusCode());
        assertTrue(actualFindUsersByPositionResult.getHeaders().isEmpty());
        verify(userRepository).findUsersByPosition(any());
    }

    @Test
    void testFindUsersByPosition2() {
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

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findUsersByPosition(any())).thenReturn(userList);
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualFindUsersByPositionResult = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .findUsersByPosition("foo");
        assertEquals(1, ((Collection<UserDto>) actualFindUsersByPositionResult.getBody()).size());
        assertTrue(actualFindUsersByPositionResult.hasBody());
        assertEquals(HttpStatus.OK, actualFindUsersByPositionResult.getStatusCode());
        assertTrue(actualFindUsersByPositionResult.getHeaders().isEmpty());
        verify(userRepository).findUsersByPosition(any());
    }

    @Test
    void testFindUsersByPosition4() {
        UserService userService = mock(UserService.class);
        when(userService.findUsersByPosition(any())).thenReturn(new ArrayList<>());
        ResponseEntity<?> actualFindUsersByPositionResult = (new UserRestController(userService))
                .findUsersByPosition("foo");
        assertTrue(actualFindUsersByPositionResult.hasBody());
        assertEquals(HttpStatus.OK, actualFindUsersByPositionResult.getStatusCode());
        assertTrue(actualFindUsersByPositionResult.getHeaders().isEmpty());
        verify(userService).findUsersByPosition(any());
    }

    @Test
    void testFindUsersByPosition6() {
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

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findUsersByPosition(any())).thenReturn(userList);
        UserMapper userMapper = mock(UserMapper.class);
        when(userMapper.userToUserDto(any())).thenReturn(new UserDto());
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        ResponseEntity<?> actualFindUsersByPositionResult = (new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Argon2PasswordEncoder())))
                .findUsersByPosition("foo");
        assertEquals(1, ((Collection<UserDto>) actualFindUsersByPositionResult.getBody()).size());
        assertTrue(actualFindUsersByPositionResult.hasBody());
        assertEquals(HttpStatus.OK, actualFindUsersByPositionResult.getStatusCode());
        assertTrue(actualFindUsersByPositionResult.getHeaders().isEmpty());
        verify(userRepository).findUsersByPosition(any());
        verify(userMapper).userToUserDto(any());
    }

    @Test
    void testSetPassword3() {
        UserService userService = mock(UserService.class);
        doNothing().when(userService).setPassword(any());
        UserRestController userRestController = new UserRestController(userService);
        ResponseEntity<?> actualSetPasswordResult = userRestController.setPassword(new PasswordDto(10L,"","Test"));
        assertNull(actualSetPasswordResult.getBody());
        assertEquals(HttpStatus.NO_CONTENT, actualSetPasswordResult.getStatusCode());
        assertTrue(actualSetPasswordResult.getHeaders().isEmpty());
        verify(userService).setPassword(any());
    }

    @Test
    void testSetPassword4() {
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

        User user2 = new User("Radoje",
                "Milosevic",
                "rmilosevic@raf.rs",
                "1231231231235",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        user2.setId(2L);
        user2.setPassword("radenko");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(any())).thenReturn(user2);
        when(userRepository.findById(any())).thenReturn(ofResult);
        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        UserRestController userRestController = new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Md4PasswordEncoder()));
        ResponseEntity<?> actualSetPasswordResult = userRestController.setPassword(new PasswordDto());
        assertNull(actualSetPasswordResult.getBody());
        assertEquals(HttpStatus.NO_CONTENT, actualSetPasswordResult.getStatusCode());
        assertTrue(actualSetPasswordResult.getHeaders().isEmpty());
        verify(userRepository).save(any());
        verify(userRepository).findById(any());
    }

    @Test
    void testUserCreate() throws Exception {
        User user = new User();
        user.setActive(true);
        user.setEmail("mradenkovic@raf.rs");
        user.setFirstName("Milos");
        user.setId(1L);
        user.setLastName("Radenkovic");
        user.setPermissions(new Permissions());
        user.setPhoneNumber("4105551212");
        user.setPosition("RAF");

        UserCreateDto userDtoCre = new UserCreateDto();
        userDtoCre.setActive(true);
        userDtoCre.setEmail("mradenkovic@raf.rs");
        userDtoCre.setFirstName("Milos");
        userDtoCre.setLastName("Radenkovic");
        userDtoCre.setPermissions(new Permissions());
        userDtoCre.setPhoneNumber("4105551212");
        userDtoCre.setPosition("RAF");

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(any())).thenReturn(user);

        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        UserRestController userRestController = new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Md4PasswordEncoder()));
        ResponseEntity<?> actualResult = userRestController.createUser(userDtoCre);

        assertNotNull(actualResult.getBody());
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertTrue(actualResult.getHeaders().isEmpty());
        assertTrue(actualResult.getBody() instanceof UserDto);
        verify(userRepository).save(any());

    }

    @Test
    void testUpdateUser() throws Exception {

        User user = new User();
        user.setActive(true);
        user.setEmail("mradenkovic@raf.rs");
        user.setFirstName("Milos");
        user.setId(1L);
        user.setLastName("Radenkovic");
        user.setPermissions(new Permissions());
        user.setPhoneNumber("4105551212");
        user.setPosition("FON");

        UserCreateDto userDto = new UserCreateDto();
        userDto.setActive(true);
        userDto.setEmail("mradenkovic@raf.rs");
        userDto.setFirstName("Milos");
        userDto.setLastName("Radenkovic");
        userDto.setPermissions(new Permissions());
        userDto.setPhoneNumber("4105551212");
        userDto.setPosition("RAF");

        UserCreateDto userDto1 = new UserCreateDto();
        userDto1.setActive(true);
        userDto1.setEmail("mradenkovic@raf.rs");
        userDto1.setFirstName("Milos");
        userDto1.setLastName("Radenkovic");
        userDto1.setPermissions(new Permissions());
        userDto1.setPhoneNumber("4105551212");
        userDto1.setPosition("FON");

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(any())).thenReturn(user);

        UserMapper userMapper = new UserMapper();
        PermissionMapper permissionMapper = new PermissionMapper();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        UserRestController userRestController = new UserRestController(
                new UserService(userRepository, userMapper, permissionMapper, taskExecutor, new Md4PasswordEncoder()));
        ResponseEntity<?> actualResult = userRestController.createUser(userDto1);

        assertNotNull(actualResult.getBody());
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertTrue(actualResult.getHeaders().isEmpty());
        assertTrue(actualResult.getBody() instanceof UserDto);
        UserDto userResult = (UserDto) actualResult.getBody();

        assertEquals(userResult.getPosition(),userDto1.getPosition());
        assertNotEquals(userResult.getPosition(),userDto.getPosition());
        verify(userRepository).save(any());
    }
}

