package com.app.quantitymeasurement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration to disable Spring Security's default protections (like CSRF) 
 * so that we can freely test our POST endpoints via Swagger UI.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // This is the magic line that fixes the 403 Forbidden error on POST requests
            .csrf(csrf -> csrf.disable()) 
            
            // Allow all requests to pass through without needing a username/password
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() 
            );
            
        return http.build();
    }
}