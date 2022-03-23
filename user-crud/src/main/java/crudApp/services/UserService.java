package crudApp.services;

import crudApp.mappers.PermissionMapper;
import crudApp.mappers.UserMapper;
import crudApp.model.*;
import crudApp.repositories.UserRepository;
import helpers.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PermissionMapper permissionMapper, ThreadPoolTaskExecutor taskExecutor, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.taskExecutor = taskExecutor;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto current() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = this.userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No logged in user found.");
        }
        return userMapper.userToUserDto(user.get());
    }

    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(userMapper.userToUserDto(u));
        }
        return dtos;
    }

    public UserDto findUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(userMapper::userToUserDto).orElse(null);
    }

    public UserDto findUserByFirstName(String firstName) {
        Optional<User> user = userRepository.findUserByFirstName(firstName);
        return user.map(userMapper::userToUserDto).orElse(null);
    }

    public UserDto findUserByLastName(String lastName) {
        Optional<User> user = userRepository.findUserByLastName(lastName);
        return user.map(userMapper::userToUserDto).orElse(null);
    }

    public UserDto findUserByPosition(String position) {
        Optional<User> user = userRepository.findUserByPosition(position);
        return user.map(userMapper::userToUserDto).orElse(null);
    }

    public UserDto createUser(UserCreateDto dto) throws Exception {
        Optional<User> user = this.userRepository.findUserByEmail(dto.getEmail());
        if (user.isEmpty()) {
            taskExecutor.execute(() -> EmailSender.getInstance().sendEmail(dto.getEmail(), "Setting your password", "https://docs.google.com/document/d/1kX7tSj7rEntLyHOQLQogigBC3cMYxS0GikjQxAd-3Tg/edit#"));
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

    public PermissionAuthority collectPermissions() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = this.userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No logged in user found.");
        }
        Collection<? extends GrantedAuthority> permissions = this.loadUserByUsername(email).getAuthorities();
        return (PermissionAuthority) permissions.toArray()[0];
    }
}
