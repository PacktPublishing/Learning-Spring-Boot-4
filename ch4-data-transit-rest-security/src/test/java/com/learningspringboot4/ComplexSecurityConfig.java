package com.learningspringboot4;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;

import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

public class ComplexSecurityConfig {


    @Bean
    SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/resources/**", "/about", "/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
                // exige ADMIN *e* DBA
                .requestMatchers("/db/**")
                .access(AuthorizationManagers.allOf(
                        hasRole("ADMIN"),
                        hasRole("DBA")
                ))
                .anyRequest().denyAll()
        );

        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
