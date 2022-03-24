package app;

import filters.LogPreFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
public class GatewayApp {
    public static void main(String[] args)  {
        SpringApplication.run(GatewayApp.class, args);
    }

    @Bean
    public LogPreFilter logPreFilter () {
        return new LogPreFilter();
    }
}
