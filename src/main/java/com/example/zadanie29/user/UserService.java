package com.example.zadanie29.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setUsername(registerUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        List<UserRole> list = Collections.singletonList(new UserRole(user, Role.ROLE_USER));
        user.setRoles(new HashSet<>(list));

        userRepository.save(user);
    }

    public List<User> findAllWithoutCurrentUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getUsername().equals(currentUser.getName()))
                .collect(Collectors.toList());
    }

    public void updateUser(UpdateUserDto userDto) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> userOptional = userRepository.findByUsername(currentUser.getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            if (userDto.getPassword() != null) {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
            }
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("Username not found.");
        }
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public void editUser(EditUserDto editUserDto) {
        Optional<User> optionalUser = userRepository.findById(editUserDto.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<UserRole> userRoles = editUserDto.getRoles()
                    .stream()
                    .map(role -> new UserRole(user, role))
                    .collect(Collectors.toSet());
            user.setRoles(userRoles);
            userRepository.save(user);
        }
    }
}
