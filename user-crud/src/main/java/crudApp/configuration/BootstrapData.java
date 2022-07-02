package crudApp.configuration;

import crudApp.model.Permissions;
import crudApp.model.User;
import crudApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private UserRepository repository;
    private PasswordEncoder encoder;

    @Autowired
    public BootstrapData(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User("Radenko","Milosevic","car@gmail.com","1111121111111","Admin","+381657779045", true, new Permissions(true,true,true,true));
        //user.setId(1l);
        user.setPassword(encoder.encode("raf"));

        User user2 = new User("Jovanka","Zagor","jzagor@gmail.com","1111113411114","Admin","+381658779045",true,new Permissions(true,true,true,true));
        //user.setId(2l);
        user2.setPassword(encoder.encode("raf"));

        this.repository.findAll().forEach(System.out::println);

        try {
            this.repository.save(user);
            this.repository.save(user2);
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("User already added");
        }
        System.out.println("Created INIT Users");
    }
}
