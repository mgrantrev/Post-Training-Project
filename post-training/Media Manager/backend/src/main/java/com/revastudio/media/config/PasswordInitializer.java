package com.revastudio.media.config;

import com.revastudio.media.entity.User;
import com.revastudio.media.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordInitializer {

    private static final Logger log = LoggerFactory.getLogger(PasswordInitializer.class);

    @Bean
    public CommandLineRunner hashPlainPasswords(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("customer1").isEmpty()) {
                User customer = new User();
                customer.setUsername("customer1");
                customer.setPassword(passwordEncoder.encode("password"));
                customer.setRole("CUSTOMER");
                customer.setCustomerId(1);
                userRepository.save(customer);
                log.info("Created seed user customer1");
            }
            if (userRepository.findByUsername("employee1").isEmpty()) {
                User employee = new User();
                employee.setUsername("employee1");
                employee.setPassword(passwordEncoder.encode("password"));
                employee.setRole("EMPLOYEE");
                employee.setEmployeeId(1);
                userRepository.save(employee);
                log.info("Created seed user employee1");
            }

            userRepository.findAll().forEach(u -> {
                String pw = u.getPassword();
                if (pw != null && !pw.startsWith("$2a$") && !pw.startsWith("$2b$")) {
                    String encoded = passwordEncoder.encode(pw);
                    u.setPassword(encoded);
                    userRepository.save(u);
                    log.info("Encoded password for user {}", u.getUsername());
                }
            });
        };
    }
}
