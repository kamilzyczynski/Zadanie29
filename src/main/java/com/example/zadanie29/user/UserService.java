package com.example.zadanie29.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public boolean checkIfAdmin(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        List<Role> adminRole = new ArrayList<>();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            adminRole = user.getRoles()
                    .stream()
                    .map(UserRole::getRole)
                    .filter(role -> role == Role.ROLE_ADMIN)
                    .collect(Collectors.toList());
        }
        return !adminRole.isEmpty();
    }

    public UpdateUserDto getUserToUpdate() {
        Optional<User> userOptional = getCurrentUser();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UpdateUserDto updateUserDto = new UpdateUserDto();
            updateUserDto.setFirstName(user.getFirstName());
            updateUserDto.setLastName(user.getLastName());
            updateUserDto.setPassword(user.getPassword());
            return updateUserDto;
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }

    public void updateUserData(UpdateUserDto userDto) {
        Optional<User> userOptional = getCurrentUser();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());

            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("Username not found.");
        }
    }

    public void updateUserPassword(UpdateUserDto userDto) {
        Optional<User> userOptional = getCurrentUser();

        if (userDto.getPassword() != null) {
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                userRepository.save(user);
            }
        }
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getCurrentUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByUsername(currentUser.getName());
    }

    @Transactional
    public void addAdmin(Long id) {
        Optional<UserRole> userRoleOptional = userRoleRepository.findByUserId(id);
        if (userRoleOptional.isPresent()) {
            UserRole userRole = userRoleOptional.get();

            userRole.setRole(Role.ROLE_ADMIN);
            userRoleRepository.save(userRole);
        }
    }

    @Transactional
    public void removeAdmin(Long id) {
        Optional<UserRole> userRoleOptional = userRoleRepository.findByUserId(id);
        if (userRoleOptional.isPresent()) {
            UserRole userRole = userRoleOptional.get();

            userRole.setRole(Role.ROLE_USER);
            userRoleRepository.save(userRole);
        }
    }
}
