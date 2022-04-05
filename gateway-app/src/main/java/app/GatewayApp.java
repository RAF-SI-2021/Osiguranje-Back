package app;

import filters.LogPreFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableZuulProxy
@ComponentScan(basePackages = {"controller", "filters", "security", "user"})
@EnableJpaRepositories("user")
@EntityScan("user")
public class GatewayApp {
    public static void main(String[] args)  {
        SpringApplication.run(GatewayApp.class, args);
    }

    @Bean
    public LogPreFilter logPreFilter () {
        return new LogPreFilter();
    }
}
