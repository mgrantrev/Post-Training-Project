package com.revastudio.media.service;

import com.revastudio.media.entity.Customer;
import com.revastudio.media.entity.User;
import com.revastudio.media.repository.CustomerRepository;
import com.revastudio.media.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerCustomer(String username, String password, String firstName, String lastName, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        // Default support rep could be assigned here if needed
        customer = customerRepository.save(customer);

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.getRoles().add("CUSTOMER");
        user.setCustomerId(customer.getCustomerId());

        return userRepository.save(user);
    }
}
