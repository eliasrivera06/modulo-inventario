package com.inventariopromart.modulo_inventariopromart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // Permite todas las solicitudes sin autenticación
            )
            .csrf(csrf -> csrf.disable());  // Deshabilita CSRF para permitir POST, PUT, DELETE
            
        return http.build();
    }
}
