package crudApp.services;

import crudApp.mappers.UserMapper;
import crudApp.model.User;
import crudApp.model.UserDto;
import crudApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
}
