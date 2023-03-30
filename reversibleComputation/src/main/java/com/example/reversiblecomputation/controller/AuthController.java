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

@Controller
public class AuthController {

    //AuthController handles requests relating to authentication

    /importing UserService and UserRepository to help with actions relating to a user's information
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    //Method which checks if user is logged in by checking if their name exists, if their name doesn't exist it means they're not logged in
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

    //Method which checks if a user has a profile picture (image)
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

    //This method checks if a user is logged in using the 'checkIfLoggedIn' method previously implemented
    //if the user is logged, then this method returns the user object from the database using a 'UserRepository' object
    //if not logged in, this method returns 'null'
    public User userObject(Authentication authentication){
        if (checkIfLoggedIn(authentication)) {
            String name = authentication.getName();
            User user = userRepository.findByEmail(name);
            return user;
        }
        return null;
    }

    //Handles GET requests at '/register', when /register is accessed, this request mapping returns the registration form
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

        // dto stores form data, we pass the dto object into the form as a model attribute
        Dto user = new Dto();
        model.addAttribute("user", user);
        return "register";
    }

    //Handles POST requests at '/register/save', after a user submits a registration form, this request mapping handles the submission
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") Dto userDto,
                               BindingResult result,
                               Model model){
        
        //First checks if the submitted email already exists in the database, if so, we return an error to the registration page
        User existingUser = userService.findUserByEmail(userDto.getEmail());
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        //Checks if there are any other errors aside from the email already being used
        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        //If there are no errors, the registration form is accepted, and the new user is created and stored into the database
        userService.saveUser(userDto);
        return "redirect:/register?success";
    }



    // Handles GET requests at '/login', when /login is accessed, this request mapping returns the login form
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
        //returns login.html
    }


}
