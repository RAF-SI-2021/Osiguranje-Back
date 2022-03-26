package crudApp.configuration;

import crudApp.mappers.PermissionMapper;
import crudApp.mappers.UserMapper;
import crudApp.repositories.UserRepository;
import crudApp.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableAsync
public class ServiceConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService service(UserRepository userRepository, UserMapper userMapper, PermissionMapper permissionMapper, ThreadPoolTaskExecutor taskExecutor) {
        return new UserService(userRepository, userMapper, permissionMapper, taskExecutor, passwordEncoder());
    }
}
