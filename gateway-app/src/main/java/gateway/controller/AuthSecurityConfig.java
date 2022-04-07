package gateway.controller;

import gateway.security.JwtAuthenticationConfig;
import gateway.security.JwtUsernamePasswordAuthenticationFilter;
import gateway.user.CustomPasswordEncoder;
import gateway.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final UserService userService;

    @Autowired
    JwtAuthenticationConfig config;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public JwtAuthenticationConfig jwtConfig() {
        return new JwtAuthenticationConfig();
    }

    @Bean
    public CustomPasswordEncoder customPasswordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .addFilterAfter(new JwtUsernamePasswordAuthenticationFilter(config, authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/debug/**").hasRole("ADMIN")
                .antMatchers("/test/admin/**").hasRole("ADMIN")
                .antMatchers("/user/logged").hasRole("USER")
                .antMatchers("/user/password").hasRole("USER")
                .antMatchers("/user/**").hasRole("ADMIN")
                .antMatchers("/test/user/**").hasRole("USER")
                .antMatchers("/test/guest/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/currentUser", false)
                .and()
                .httpBasic();
    }
}
