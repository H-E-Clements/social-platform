package com.example.reversiblecomputation.controller;

import autovalue.shaded.com.google.common.base.Charsets;
import autovalue.shaded.com.google.common.hash.Hashing;
import com.example.reversiblecomputation.domain.*;
import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.dto.Dto;
import com.example.reversiblecomputation.repository.DocumentRepository;
import com.example.reversiblecomputation.repository.PostRepository;
import com.example.reversiblecomputation.repository.UserRepository;
import com.example.reversiblecomputation.service.CloudService;
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
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {


    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    CloudService cloudService;

    public boolean checkIfLoggedIn(Authentication authentication, Model model){
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
    // homepage
    @GetMapping("/")
    public String home(Authentication authentication, Model model){

        // checks if logged in for nav bar
        boolean navUser = checkIfLoggedIn(authentication, model);
        model.addAttribute("navUser", navUser);
        return "home";
    }

    // feed page
    @RequestMapping("/feed")
    public String feed(Authentication authentication, Model model){
        // checks if logged in for nav bar
        boolean navUser = checkIfLoggedIn(authentication, model);
        model.addAttribute("navUser", navUser);
        return "feed";
    }

    @GetMapping("/events")
    public String events(Authentication authentication, Model model){
        // checks if logged in for nav bar
        boolean navUser = checkIfLoggedIn(authentication, model);
        model.addAttribute("navUser", navUser);
        return "feed";
    }

    @GetMapping("/upload")
    public String upload(Model model){
        PackagedDocument document = new PackagedDocument();
        Post post = new Post();
        model.addAttribute("post", post);
        model.addAttribute("document", document);
        model.addAttribute("name", "");
        return "upload";
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
            return "redirect:/logout";
        }
        else if (param.equals("name")) {
            user.setName(userDto.getFirstName() + " " + userDto.getLastName());
            userRepository.save(user);
            return "redirect:/profile";
        }
        else if (param.equals("password")) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
            return "redirect:/logout";
        }

        else if (param.equals("description")) {
            user.setDescription(userDto.getDescription());
            userRepository.save(user);
            return "redirect:/profile";
        }

        else if (param.equals("age")) {
            user.setAge(userDto.getAge());
            userRepository.save(user);
            return "redirect:/profile";
        }

        else if (param.equals("location")) {
            user.setLocation(userDto.getLocation());
            userRepository.save(user);
            return "redirect:/profile";
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

    @PostMapping("/newDocument")
    public String createDocument(@ModelAttribute Post post, @RequestParam MultipartFile file, @RequestParam String fileName) throws IOException {
        if (file.getSize() > 0) {
            System.out.println("---------------------->"+file.getName());
            System.out.println("---------------------->"+file.getSize());
            Document document = new Document();
            int lastSlash = fileName.lastIndexOf("\\");
            int extentionIndex = fileName.lastIndexOf(".");
            document.setName(fileName.substring(lastSlash+1, extentionIndex));
            document.setExtension(fileName.substring(extentionIndex));
            UUID randomId = UUID.randomUUID();
            document.setId(randomId.toString());
            documentRepository.save(document);

            cloudService.fileUpload(file.getBytes(), randomId.toString());
//            documentRepository.save(document);
        }
        return "upload";
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
    
    @GetMapping("/post")
    public String post(Model model){
        Post post = new Post();
        model.addAttribute("post", post);
        return "post";
    }

//    @GetMapping("/upload")
//    public String upload(Model model){
//        Post post = new Post();
//        model.addAttribute("post", post);
//        return "upload";
//    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication){
        String username;
        try {
            User object = userRepository.findByEmail(authentication.getName());
            model.addAttribute("email", object.getEmail());
            model.addAttribute("description", object.getDescription());
            model.addAttribute("user", object.getName());
            model.addAttribute("age", object.getAge());
            model.addAttribute("location", object.getLocation());

        }

        catch(Exception e) {
            username = "Not Logged In";
        }

        return "profile";
    }
}
