package app;

import bootstrap.DataLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"controllers", "configuration", "model", "repositories","services"})
@EnableJpaRepositories("repositories")
@EntityScan("model")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(DataLoader.class, args);
    }
}
