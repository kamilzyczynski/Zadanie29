package com.example.zadanie29.config;

import com.example.zadanie29.user.User;
import com.example.zadanie29.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<SimpleGrantedAuthority> roles = Set.of(new SimpleGrantedAuthority(user.getRole().name()));

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
        }

        throw new UsernameNotFoundException("Username " + username + " not found");
    }
}
