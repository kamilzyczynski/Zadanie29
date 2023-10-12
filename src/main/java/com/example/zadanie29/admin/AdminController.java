package com.example.zadanie29.admin;

import com.example.zadanie29.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/addAdmin/user/{id}")
    public String addAdmin(@PathVariable Long id) {
        userService.addAdmin(id);
        return "redirect:/admin";
    }

    @GetMapping("/removeAdmin/user/{id}")
    public String removeAdmin(@PathVariable Long id) {
        userService.removeAdmin(id);
        return "redirect:/admin";
    }
}
