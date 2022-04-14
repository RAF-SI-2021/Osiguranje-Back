package crudApp.repositories;

import crudApp.model.Permissions;
import crudApp.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository testUserRepository;

    @AfterEach
    void tearDown() {
        testUserRepository.deleteAll();
        testUserRepository.flush();
    }

    @Test
    void checkIfUserIsFoundForEmail() {
        //given
        String testEmail = "mradenkovic@raf.rs";
        User user = new User("Milos",
                "Radenkovic",
                testEmail,
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        testUserRepository.save(user);
        //when
        Optional<User> radenkovic = testUserRepository.findUserByEmail(testEmail);
        //then
        assertThat(radenkovic.isPresent()).isTrue();
    }

    /*
    @Test
    void checkIfAddingMultipleUsersWithSameEmailThrowsException() {
        //given
        String testEmail = "mradenkovic@raf.rs";
        User user1 = new User("Milos",
                "Radenkovic",
                testEmail,
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        testUserRepository.save(user1);
        User user2 = new User("Milos",
                "Radenkovic",
                testEmail,
                "1231231231235",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        //when
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            testUserRepository.save(user2);
        });
        //then
        assertThat(testUserRepository.findUsersByFirstName("Milos").size() == 1).isTrue();
    }
    */


    @Test
    void checkIfUserIsNotFoundForEmail() {
        //given
        String testEmail = "mradenkovic@raf.rs";
        //when
        Optional<User> radenkovic = testUserRepository.findUserByEmail(testEmail);
        //then
        assertThat(radenkovic.isEmpty()).isTrue();
    }

    @Test
    void checkIfUsersAreFoundForFirstName() {
        //given
        String firstName = "Milos";
        User user = new User(firstName,
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        testUserRepository.save(user);
        user = new User(firstName,
                "Milosevic",
                "mmilosevic@raf.rs",
                "1231231231235",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        testUserRepository.save(user);
        //when
        List<User> usersForName = testUserRepository.findUsersByFirstName(firstName);
        //then
        assertThat(!usersForName.isEmpty()).isTrue();
        assertThat(usersForName.size() == 2).isTrue();
    }

    @Test
    void checkIfThereAreNoUsersForFirstName() {
        //given
        String firstName = "Milos";
        //when
        List<User> usersForName = testUserRepository.findUsersByFirstName(firstName);
        //then
        assertThat(usersForName.isEmpty()).isTrue();
    }

    @Test
    void checkIfUsersAreFoundForLastName() {
        //given
        String lastName = "Radenkovic";
        User user = new User("Milos",
                lastName,
                "mradenkovic@raf.rs",
                "1231231231234",
                "RAF",
                "0641231234",
                true,
                new Permissions());
        testUserRepository.save(user);
        user = new User("Radoje",
                lastName,
                "rradenkovic@raf.rs",
                "1231231231235",
                "RAF",
                "0641231235",
                true,
                new Permissions());
        testUserRepository.save(user);
        //when
        List<User> usersForName = testUserRepository.findUsersByLastName(lastName);
        //then
        assertThat(!usersForName.isEmpty()).isTrue();
        assertThat(usersForName.size() == 2).isTrue();
    }

    @Test
    void checkIfThereAreNoUsersForLastName() {
        //given
        String lastName = "Radenkovic";
        //when
        List<User> usersForName = testUserRepository.findUsersByLastName(lastName);
        //then
        assertThat(usersForName.isEmpty()).isTrue();
    }

    @Test
    void checkIfUsersAreFoundForPosition() {
        //given
        String position = "RAF";
        User user = new User("Milos",
                "Radenkovic",
                "mradenkovic@raf.rs",
                "1231231231234",
                position,
                "0641231234",
                true,
                new Permissions());
        testUserRepository.save(user);
        user = new User("Radoje",
                "Milosevic",
                "rradenkovic@raf.rs",
                "1231231231235",
                position,
                "0641231235",
                true,
                new Permissions());
        testUserRepository.save(user);
        //when
        List<User> usersForName = testUserRepository.findUsersByPosition(position);
        //then
        assertThat(!usersForName.isEmpty()).isTrue();
        assertThat(usersForName.size() == 2).isTrue();
    }

    @Test
    void checkIfThereAreNoUsersForPosition() {
        //given
        String position = "RAF";
        //when
        List<User> usersForName = testUserRepository.findUsersByPosition(position);
        //then
        assertThat(usersForName.isEmpty()).isTrue();
    }
}