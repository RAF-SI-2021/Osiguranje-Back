package crudApp.services;

import crudApp.mappers.PermissionMapper;
import crudApp.mappers.UserMapper;
import crudApp.model.PermissionAuthority;
import crudApp.model.User;
import crudApp.model.UserDto;
import crudApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PermissionMapper permissionMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
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

    public void save(User user) {
        userRepository.save(user);
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
        Collection<? extends GrantedAuthority> permissions = this.loadUserByUsername(email).getAuthorities();
        return (PermissionAuthority) permissions.toArray()[0];
    }
}
