package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.models.User;
import com.example.reversiblecomputation.dto.UserDTO;
import com.example.reversiblecomputation.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Redirects to the home page.
     */
    @GetMapping("/")
    public String home(){
        return "home";
    }

    /**
     * Redirects to the registration page.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        return "register";
    }

    /**
     * Handles registration request.
     * @param userDto The new user to save
     */
    @PostMapping("/register")
    public String registration(
            @Valid @ModelAttribute("user") UserDTO userDto,
            BindingResult result,
            Model model
    ) {
        // Check for existing users
        User existingUser = userService.getUser(
                null,
                userDto.getUsername(),
                null,
                null,
                null,
                null,
                userDto.getEmailAddress(),
                null
        );

        if(existingUser != null){
            result.rejectValue("username", null,
                    "An account with that username or email already exists.");
            result.rejectValue("emailAddress", null,
                    "An account with that username or email already exists.");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        // Attempt to save user
        User user = new User(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    // lists all users
    @GetMapping("/users")
    public String users(Model model){
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    // login page
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}