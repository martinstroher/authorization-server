package com.example.authorizationserver.config;

import com.example.authorizationserver.entity.User;
import com.example.authorizationserver.repository.UserRepository;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .requestMatchers(HttpMethod.POST, "/api/ingredients")
                .hasAuthority("SCOPE_writeIngredients")
                .requestMatchers(HttpMethod.DELETE, "/api/ingredients")
                .hasAuthority("SCOPE_deleteIngredients")
                
                .and()
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());
    }
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests(authorizeRequests ->
                authorizeRequests.anyRequest().authenticated())
                .formLogin()
                .and().build();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository repository) {
        return username -> repository.findById(username).orElseThrow();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner dataLoader(UserRepository repository, PasswordEncoder encoder) {
        return args -> {
//            repository.save(new User("habuma", encoder.encode("password"), List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
//            repository.save(new User("tacochef", encoder.encode("password"), List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
            repository.save(new User("tacochef", encoder.encode("password")));
        };
    }
}
