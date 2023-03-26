package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.*;
import com.example.reversiblecomputation.dto.Dto;
import com.example.reversiblecomputation.dto.SearchDto;
import com.example.reversiblecomputation.repository.UserRepository;
import com.example.reversiblecomputation.service.CloudService;
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
import java.util.HashSet;
import java.util.Set;

@Controller
public class MainController {

    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    CloudService cloudService;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/pfp";


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

    // shows edit form
    @GetMapping("/edit")
    public String showEditForm(Model model, Authentication authentication){
        // dto stores form data
        Dto user = new Dto();
        model.addAttribute("user", user);
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }
        return "edit";
    }

    // handles after user submits
    @PostMapping("/edit/save/{param}")
    public String edit(@Valid @ModelAttribute("user") @PathVariable(value = "param") String param,
                               Dto userDto,
                               BindingResult result,
                               Model model,
                               Authentication authentication,
                               @RequestParam(name="image", required = false) MultipartFile file) throws IOException {
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

        else if (param.equals("image")) {
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, Long.toString(user.getId())+".png");
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
            user.setImg(true);
            userRepository.save(user);
            return "redirect:/edit";
        }

        return "redirect:/edit?success";
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

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication){
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }

        // profile info if no id attached
        model.addAttribute("email", searchAndIdentifyService.userObject(authentication).getEmail());
        model.addAttribute("description", searchAndIdentifyService.userObject(authentication).getDescription());
        model.addAttribute("user", searchAndIdentifyService.userObject(authentication).getName());
        model.addAttribute("age", searchAndIdentifyService.userObject(authentication).getAge());
        model.addAttribute("location", searchAndIdentifyService.userObject(authentication).getLocation());


        return "profile";
    }

    @GetMapping("/profile/{id}")
    public String foreignProfile(@PathVariable(value = "id") Long id ,Model model, Authentication authentication){
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }

        // profile info if id attached
        model.addAttribute("email", searchAndIdentifyService.searchUserObjectId(authentication, id).getEmail());
        model.addAttribute("description", searchAndIdentifyService.searchUserObjectId(authentication, id).getDescription());
        model.addAttribute("user", searchAndIdentifyService.searchUserObjectId(authentication, id).getName());
        model.addAttribute("age", searchAndIdentifyService.searchUserObjectId(authentication, id).getAge());
        model.addAttribute("location", searchAndIdentifyService.searchUserObjectId(authentication, id).getLocation());
        return "profile";
        }

    @GetMapping("/search")
    public String search(Model model, Authentication authentication, @ModelAttribute("query") SearchDto searchDto) {
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId() + ".png");
        }
        catch (Exception e) {
        }
        //splits up string into individual queries
        String[] queries = searchDto.getQuery().split("\\s+");
        Set<User> uniqueUsers = new HashSet<User>();
        //looks for each query in database (only user names)
        for (String query : queries) {
            for (User user : userRepository.findByNameContainingIgnoreCase(query)) {
                uniqueUsers.add(user);
            }
        }
        model.addAttribute("uniqueUsers", uniqueUsers);
        return "feed";
    }
}
