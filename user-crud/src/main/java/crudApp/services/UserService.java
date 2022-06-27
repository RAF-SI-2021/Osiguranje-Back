package crudApp.services;

import crudApp.dto.PasswordDto;
import crudApp.dto.UserCreateDto;
import crudApp.dto.UserDto;
import crudApp.mappers.PermissionMapper;
import crudApp.mappers.UserMapper;
import crudApp.model.*;
import crudApp.repositories.UserRepository;
import helpers.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PermissionMapper permissionMapper, ThreadPoolTaskExecutor taskExecutor, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.taskExecutor = taskExecutor;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(userMapper.userToUserDto(u));
        }
        return dtos;
    }

    public UserDto findUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::userToUserDto).orElse(null);
    }

    public UserDto findUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(userMapper::userToUserDto).orElse(null);
    }

    public Long findUserIdByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(User::getId).orElse(null);
    }

    public List<UserDto> findUsersByFirstName(String firstName) {
        List<User> users = userRepository.findUsersByFirstName(firstName);
        List<UserDto> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(userMapper.userToUserDto(u));
        }
        return dtos;
    }

    public List<UserDto> findUsersByLastName(String lastName) {
        List<User> users = userRepository.findUsersByLastName(lastName);
        List<UserDto> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(userMapper.userToUserDto(u));
        }
        return dtos;
    }

    public List<UserDto> findUsersByPosition(String position) {
        List<User> users = userRepository.findUsersByPosition(position);
        List<UserDto> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(userMapper.userToUserDto(u));
        }
        return dtos;
    }

    public UserDto createUser(UserCreateDto dto) throws Exception {
        Optional<User> user = this.userRepository.findUserByEmail(dto.getEmail());
        if (user.isEmpty()) {
            //taskExecutor.execute(() -> EmailSender.getInstance().sendEmail(dto.getEmail(), "Setting your password", "https://docs.google.com/document/d/1kX7tSj7rEntLyHOQLQogigBC3cMYxS0GikjQxAd-3Tg/edit#"));
            User u = userRepository.save(userMapper.userCreateDtoToUser(dto));
            return userMapper.userToUserDto(u);
        } else {
            throw new Exception("User already exists.");
        }
    }

    public UserDto updateUser(UserDto dto) {
        Optional<User> user = this.userRepository.findUserByEmail(dto.getEmail());
        if (user.isPresent()) {
            user.get().setFirstName(dto.getFirstName());
            user.get().setLastName(dto.getLastName());
            user.get().setEmail(dto.getEmail());
            user.get().setPosition(dto.getPosition());
            user.get().setActive(dto.getActive());
            userRepository.save(userMapper.userDtoToUser(dto));
            return dto;
        } else {
            throw new UsernameNotFoundException("No such user.");
        }
    }

    public void setPassword(PasswordDto dto) {
        Optional<User> u = this.userRepository.findById(dto.getId());
        if (u.isPresent()) {
            User user = u.get();
            dto.setCurrentPassword(passwordEncoder.encode(dto.getCurrentPassword()));
            dto.setNewPassword(passwordEncoder.encode(dto.getNewPassword()));
            if (user.getPassword() == null || user.getPassword().equals(dto.getCurrentPassword())) {
                user.setPassword(dto.getNewPassword());
            }
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("No such user.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + email + "not found.");
        }
        List<PermissionAuthority> permissions = new ArrayList<>();
        permissions.add(permissionMapper.permissionsToPermissionAuthority(user.get().getPermissions()));

        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), permissions);
    }
}
