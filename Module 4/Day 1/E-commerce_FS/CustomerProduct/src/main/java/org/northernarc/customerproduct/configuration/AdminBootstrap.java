package org.northernarc.customerproduct.configuration;

import jakarta.annotation.PostConstruct;
import org.northernarc.customerproduct.model.User;
import org.northernarc.customerproduct.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrap {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    @PostConstruct
//    public void seedAdminUser() {
//        if (userRepository.findByUsernameIgnoreCase("Admin123").isPresent()) {
//            return;
//        }

//        User admin = new User();
//        admin.setUsername("Admin123");
//        admin.setPassword(passwordEncoder.encode("Admin@123"));
//        admin.setRole("ADMIN");
//        admin.setName("Administrator");
//        userRepository.save(admin);
//    }
}
