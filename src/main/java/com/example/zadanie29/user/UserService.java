package com.example.zadanie29.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setUsername(registerUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
    }

    public List<User> findAllWithoutCurrentUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getUsername().equals(currentUser.getName()))
                .collect(Collectors.toList());
    }

    public Optional<UpdateUserDto> getUserToUpdate() {
        Optional<User> userOptional = getCurrentUser();
        return userOptional.map(user -> {
                UpdateUserDto updateUserDto = new UpdateUserDto();
                updateUserDto.setFirstName(user.getFirstName());
                updateUserDto.setLastName(user.getLastName());
                return updateUserDto;
            }
    );
    }

    public void updateUserData(UpdateUserDto userDto) {
        Optional<User> userOptional = getCurrentUser();

        User user = userOptional.get();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        userRepository.save(user);
    }

    public void updateUserPassword(String password) {
        Optional<User> userOptional = getCurrentUser();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }
    }

    public Optional<User> getCurrentUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByUsername(currentUser.getName());
    }

    @Transactional
    public void addAdmin(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setRole(Role.ROLE_ADMIN);
            userRepository.save(user);
        }
    }

    @Transactional
    public void removeAdmin(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setRole(Role.ROLE_USER);
            userRepository.save(user);
        }
    }
}
