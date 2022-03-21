package crudApp.repositories;

import crudApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByFirstName(String firstName);
    Optional<User> findUserByLastName(String lastName);
    Optional<User> findUserByPosition(String position);
}
