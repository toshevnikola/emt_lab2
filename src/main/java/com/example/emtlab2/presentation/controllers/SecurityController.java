package com.example.emtlab2.presentation.controllers;

import com.example.emtlab2.model.User;
import com.example.emtlab2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SecurityController {
    private UserService userService;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "admin";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(@Valid User user, BindingResult bindingResult, Model model) {
        boolean b = this.userService.userExists(user.getUsername());
        if (b) {
            return "redirect:/register?error=Username already taken";
        }
        this.userService.save(user);
        return "redirect:/login";

    }
}
