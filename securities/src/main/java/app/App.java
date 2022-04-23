package app;

import app.bootstrap.BootstrapData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"app", "app.bootstrap", "app.controllers", "app.configuration", "app.model", "app.repositories", "app.services"})
@EnableJpaRepositories("app.repositories")
@EntityScan("app.model")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
