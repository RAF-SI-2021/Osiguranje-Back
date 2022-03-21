package crudApp.services;

import crudApp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> findAll();
    public Optional<User> findUserByEmail(String email);
    public Optional<User> findUserByFirstName(String firstName);
    public Optional<User> findUserByLastName(String lastName);
    public Optional<User> findUserByPosition(String position);
    public void save(User user);

}
