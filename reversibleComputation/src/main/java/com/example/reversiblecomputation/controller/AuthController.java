package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.User;
import com.example.reversiblecomputation.dto.Dto;
import com.example.reversiblecomputation.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.Authenticator;
import java.util.List;

//@RequestMapping(value = "/auth")
@Controller
public class AuthController {


    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    // shows registration form
    @GetMapping("/register")
    public String showRegistrationForm(Authentication authentication, Model model){
        boolean navUser;
        try {
            authentication.getName();
            navUser = true;
        }
        catch(Exception e) {
            navUser = false;
        }
        model.addAttribute("navUser", navUser);
        // dto stores form data
        Dto user = new Dto();
        model.addAttribute("user", user);
        return "register";
    }

    // handles after user submits
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") Dto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    // lists all users
    @GetMapping("/users")
    public String users(Model model){
        List<Dto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }



    // login page
    @GetMapping("/login")
    public String login(Authentication authentication, Model model){
        boolean user;
        try {
            authentication.getName();
            user = true;
        }
        catch(Exception e) {
            user = false;
        }
        model.addAttribute("user", user);
        return "login";
    }


}
