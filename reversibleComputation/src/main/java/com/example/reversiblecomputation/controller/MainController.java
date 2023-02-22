package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.domain.User;
import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.dto.Dto;
import com.example.reversiblecomputation.repository.PostRepository;
import com.example.reversiblecomputation.repository.UserRepository;
import com.example.reversiblecomputation.service.CustomDetailService;
import com.example.reversiblecomputation.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // homepage
    @GetMapping("/")
    public String home(){
        return "home";
    }

    // feed page
    @RequestMapping("/feed")
    public String feed(){
        return "feed";
    }

    @GetMapping("/events")
    public String events(){
        return "feed";
    }

    // shows edit form
    @GetMapping("/edit")
    public String showEditForm(Model model){
        // dto stores form data
        Dto user = new Dto();
        model.addAttribute("user", user);
        return "edit";
    }

    // handles after user submits
    @PostMapping("/edit/save/{param}")
    public String registration(@Valid @ModelAttribute("user") @PathVariable(value = "param") String param, Dto userDto,
                               BindingResult result,
                               Model model,
                               Authentication authentication){
        System.out.println(param);
        User existingUser = userService.findUserByEmail(userDto.getEmail());
        String currentUser = authentication.getName();
        User user = userRepository.findByEmail(currentUser);
        if (param.equals("email")) {

            if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
                result.rejectValue("email", null,
                        "There is already an account registered with the same email");
            }
    
            if(result.hasErrors()){
                model.addAttribute("user", userDto);
                return "/edit";
            }
            user.setEmail(userDto.getEmail());
            userRepository.save(user);
        }
        else if (param.equals("name")) {
            user.setName(userDto.getFirstName() + " " + userDto.getLastName());
            userRepository.save(user);
        }
        else if (param.equals("password")) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
            return "redirect:/logout";
        }
        return "redirect:/edit?success";
    }

    @PostMapping("/newPost")
    public String createPost(@Valid @ModelAttribute("post") Post post) {
        if (post != null) {
            postRepository.save(post);
        }
        return "feed";
    }

    @GetMapping("/username")
    public String username(Model model, Authentication authentication){
        String user;
        try {
            user = authentication.getName();
        }
        catch(Exception e) {
            user = "Not Logged In";
        }

        model.addAttribute("user", user);
        return "username";
    }
    
    @GetMapping("/post")
    public String post(Model model){
        Post post = new Post();
        model.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/upload")
    public String upload(Model model){
        Post post = new Post();
        model.addAttribute("post", post);
        return "upload";
    }
}
