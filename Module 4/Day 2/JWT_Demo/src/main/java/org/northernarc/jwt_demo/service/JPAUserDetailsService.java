package org.northernarc.jwt_demo.service;
import jakarta.annotation.PostConstruct;
import org.northernarc.jwt_demo.model.JpaUser;
import org.northernarc.jwt_demo.repository.JpaUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class JPAUserDetailsService implements UserDetailsService {
    @Autowired
    private JpaUserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JpaUser jpaUser =userRepo.findByUsername(username);
        if(jpaUser==null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return User.builder()
                .username(jpaUser.getUsername())
                .password(jpaUser.getPassword())
                .roles(jpaUser.getRole())
                .build();
    }
//    @PostConstruct
//    public void init() {
//        JpaUser jpaUser =new  JpaUser();
//        jpaUser.setUsername("admin");
//        jpaUser.setPassword(passwordEncoder.encode("admin123"));
//        jpaUser.setRole("ADMIN");
//        JpaUser jpaUser2 =new  JpaUser();
//        jpaUser2.setUsername("user");
//        jpaUser2.setPassword(passwordEncoder.encode("user123"));
//        jpaUser2.setRole("USER");
//        userRepo.save(jpaUser);
//        userRepo.save(jpaUser2);
//    }
}