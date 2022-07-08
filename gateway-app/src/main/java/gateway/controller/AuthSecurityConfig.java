package gateway.controller;

import gateway.security.JwtAuthenticationConfig;
import gateway.security.JwtTokenAuthenticationFilter;
import gateway.security.JwtUsernamePasswordAuthenticationFilter;
import gateway.user.CustomPasswordEncoder;
import gateway.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final JwtAuthenticationConfig config;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public AuthSecurityConfig(UserService userService, JwtAuthenticationConfig config, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.config = config;
        this.passwordEncoder = passwordEncoder;
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAfter(new JwtUsernamePasswordAuthenticationFilter(config, authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenAuthenticationFilter(config),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(config.getUrl()).permitAll()
                .antMatchers("/debug/**").hasRole("ADMIN")
                .antMatchers("/test/admin/**").hasRole("ADMIN")
                .antMatchers("/user/logged").hasRole("USER")
                .antMatchers("/user/password").hasRole("USER")
                .antMatchers("/user/**").hasRole("ADMIN")
                .antMatchers("/test/user/**").hasRole("USER")
                .antMatchers("/test/guest/**").permitAll()
                .antMatchers("/exchange/**").hasRole("USER")
                .antMatchers("/securities/**").hasRole("USER")
                .antMatchers("/buyingmarket/**").hasRole("USER")
                .antMatchers("/stockinfo/**").hasRole("USER")
                .antMatchers("/account-transaction/**").hasRole("USER")
                .antMatchers("/otc/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .httpBasic().disable();
    }
}
