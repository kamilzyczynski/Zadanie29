package com.example.zadanie29.web;

import com.example.zadanie29.user.RegisterUserDto;
import com.example.zadanie29.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        Boolean registrationSuccess = (Boolean) model.asMap().get("registrationSuccess");
        if (registrationSuccess != null) {
            model.addAttribute("registrationSuccess", registrationSuccess);
        }
        return "loginForm";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new RegisterUserDto());
        return "registerForm";
    }

    @PostMapping("/register")
    public String registerUser(RegisterUserDto registerUserDto, RedirectAttributes redirectAttributes) {
        userService.registerUser(registerUserDto);
        boolean registrationSuccess = true;
        redirectAttributes.addFlashAttribute("registrationSuccess", registrationSuccess);
        return "redirect:/login";
    }
}
