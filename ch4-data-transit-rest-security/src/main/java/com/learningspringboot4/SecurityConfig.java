package com.learningspringboot4;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  CommandLineRunner initUsers(UserManagementRepository repository, PasswordEncoder encoder) {
    return args -> {
      repository.save(new UserAccount("alice", encoder.encode("password"), "ROLE_USER"));
      repository.save(new UserAccount("bob", encoder.encode("password"), "ROLE_USER"));
      repository.save(new UserAccount("admin", encoder.encode("password"), "ROLE_ADMIN"));
    };
  }

//  @Bean
//  CommandLineRunner initUsers(UserManagementRepository repository) {
//    return args -> {
//      repository.save(new UserAccount("alice", "password", "ROLE_USER"));
//      repository.save(new UserAccount("bob", "password", "ROLE_USER"));
//      repository.save(new UserAccount("admin", "password", "ROLE_ADMIN"));
//    };
//  }

  @Bean
  UserDetailsService userService(UserRepository repo) {
    return username -> repo.findByUsername(username).asUser();
  }


  @Bean
  SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {

    http
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login").permitAll()
            .requestMatchers("/", "/search").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
            .requestMatchers("/admin").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/delete/**", "/new-video").authenticated()
            .anyRequest().denyAll()
    );

    http.formLogin(form -> {});
    http.httpBasic(basic -> {});

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
