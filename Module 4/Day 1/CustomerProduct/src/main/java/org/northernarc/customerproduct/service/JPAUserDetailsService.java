package org.northernarc.customerproduct.service;
import org.northernarc.customerproduct.model.JpaUser;
import org.northernarc.customerproduct.repository.JpaUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class JPAUserDetailsService implements UserDetailsService {
    @Autowired
    private JpaUserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JpaUser jpaUser = userRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return User.builder()
                .username(jpaUser.getUsername())
                .password(jpaUser.getPassword())
                .roles(jpaUser.getRole())
                .build();
    }
}