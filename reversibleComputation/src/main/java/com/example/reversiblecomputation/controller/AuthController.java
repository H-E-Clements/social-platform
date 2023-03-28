package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.User;
import com.example.reversiblecomputation.dto.Dto;
import com.example.reversiblecomputation.repository.UserRepository;
import com.example.reversiblecomputation.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserRepository userRepository;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    public boolean checkIfLoggedIn(Authentication authentication){
        boolean user;
        try {
            authentication.getName();
            user = true;
        }
        catch(Exception e) {
            user = false;
        }

        return user;
    }

    public boolean checkImg(Authentication authentication){
        if (checkIfLoggedIn(authentication)) {
            String name = authentication.getName();
            User user = userRepository.findByEmail(name);
            if(user != null) {
                if (user.isImg()) {
                    return true;
                }
            }
        }
        return false;
    }
    public User userObject(Authentication authentication){
        if (checkIfLoggedIn(authentication)) {
            String name = authentication.getName();
            User user = userRepository.findByEmail(name);
            return user;
        }
        return null;
    }

    // shows registration form
    @GetMapping("/register")
    public String showRegistrationForm(Authentication authentication, Model model){

        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }

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



    // login page
    @GetMapping("/login")
    public String login(Authentication authentication, Model model){
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }
        return "login";
    }


}
