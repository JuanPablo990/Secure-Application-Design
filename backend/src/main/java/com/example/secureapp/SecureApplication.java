package com.example.secureapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SecureApplication {

    private final Map<String, String> users = new HashMap<>(); // username -> hashed_password
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        SpringApplication.run(SecureApplication.class, args);
    }

    public SecureApplication() {
        // Usuarios de prueba
        users.put("admin", encoder.encode("admin123"));
        users.put("juanp", encoder.encode("password"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String user = credentials.get("username");
        String pass = credentials.get("password");

        if (users.containsKey(user) && encoder.matches(pass, users.get(user))) {
            return ResponseEntity.ok().body(Map.of("status", "ok", "user", user));
        }
        return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Login fallido"));
    }
}

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // Muy simple para el lab
        return http.build();
    }
}
