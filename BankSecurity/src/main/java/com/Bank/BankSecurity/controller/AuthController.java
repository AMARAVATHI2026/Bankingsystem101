package com.Bank.BankSecurity.controller;

import com.Bank.BankSecurity.model.User;
import com.Bank.BankSecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null)  model.addAttribute("errorMsg", "Invalid email or password.");
        if (logout != null) model.addAttribute("logoutMsg", "You have been logged out.");
        return "login";
    }

    @GetMapping("/auth/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/auth/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) return "register";
        try {
            userService.register(user);
            redirectAttributes.addFlashAttribute("successMsg", "Account created! Please log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "register";
        }
    }
}
