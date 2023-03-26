package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.*;
import com.example.reversiblecomputation.dto.Dto;
import com.example.reversiblecomputation.dto.SearchDto;
import com.example.reversiblecomputation.repository.FeedRepository;
import com.example.reversiblecomputation.repository.UserRepository;
import com.example.reversiblecomputation.service.CloudService;
import com.example.reversiblecomputation.service.DateSortService;
import com.example.reversiblecomputation.service.SearchAndIdentifyService;
import com.example.reversiblecomputation.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {

    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    CloudService cloudService;



    // homepage
    @GetMapping("/")
    public String home(Authentication authentication, Model model){

        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
        model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }

        return "home";
    }

    @GetMapping("/username")
    public String username(Model model, Authentication authentication){
        String user;
        try {
            user = authentication.getName();
            User object = userRepository.findByEmail(user);
            model.addAttribute("email", object.getEmail());
            model.addAttribute("description", object.getDescription());
            model.addAttribute("user", object.getName());

        }

        catch(Exception e) {
            user = "Not Logged In";
        }

        return "username";
    }

}
