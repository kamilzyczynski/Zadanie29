package com.example.zadanie29.admin;

import com.example.zadanie29.user.EditUserDto;
import com.example.zadanie29.user.User;
import com.example.zadanie29.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminController {
    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showAdminPanel(Model model) {
        model.addAttribute("users", userService.findAllWithoutCurrentUser());
        return "adminPanel";
    }

    @GetMapping("/editUser/{id}")
    public String editUserRoleForm(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", new EditUserDto(id));
        } else {
            throw new IllegalArgumentException("User with " + id + " not found.");
        }
        return "editUserRoleForm";
    }

    @PostMapping("/editUser")
    public String editUser(EditUserDto editUserDto) {
        userService.editUser(editUserDto);
        return "redirect:/admin";
    }
}
