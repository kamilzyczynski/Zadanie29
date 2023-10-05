package com.example.zadanie29.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/updateUser")
    public String showUpdateForm(Model model) {
        model.addAttribute("user", new UpdateUserDto());
        return "updateForm";
    }

    @PostMapping("/updateUser")
    public String update(UpdateUserDto userDto) {
        userService.updateUser(userDto);
        return "redirect:/";
    }
}
