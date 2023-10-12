package com.example.zadanie29.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/updateUser")
    public String showUpdateForm(Model model) {
        model.addAttribute("user", userService.getUserToUpdate());
        return "updateForm";
    }

    @PostMapping("/updateUserData")
    public String updateData(UpdateUserDto userDto) {
        userService.updateUserData(userDto);
        return "redirect:/";
    }

    @PostMapping("/updateUserPassword")
    public String updatePassword(@RequestParam String password) {
        userService.updateUserPassword(password);
        return "redirect:/";
    }
}
