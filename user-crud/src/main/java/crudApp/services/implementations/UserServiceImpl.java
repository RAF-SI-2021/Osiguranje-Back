package crudApp.services.implementations;

import crudApp.model.User;
import crudApp.repositories.UserRepository;
import crudApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<User> findUserByFirstName(String firstName) {
        return userRepository.findUserByFirstName(firstName);
    }

    @Override
    public Optional<User> findUserByLastName(String lastName) {
        return userRepository.findUserByLastName(lastName);
    }

    @Override
    public Optional<User> findUserByPosition(String position) {
        return userRepository.findUserByPosition(position);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }


}
