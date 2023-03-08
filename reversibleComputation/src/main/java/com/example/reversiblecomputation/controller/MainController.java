package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.Document;
import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.domain.User;
import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.dto.Dto;
import com.example.reversiblecomputation.dto.SearchDto;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/pfp";

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
            if (user.isImg()){
                return true;
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

    public User searchUserObjectId(Authentication authentication, Long id){
        try {
            User userObject;
            Optional<User> user = userRepository.findById(id);
            userObject = user.get();
            return userObject;
        }
        catch(Exception e) {
            return null;
        }
    }


    // homepage
    @GetMapping("/")
    public String home(Authentication authentication, Model model){

        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
        model.addAttribute("id", userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }

        return "home";
    }

    // feed page
    @RequestMapping("/feed")
    public String feed(Authentication authentication, Model model){
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }
        SearchDto searchDto = new SearchDto();
        model.addAttribute("query", searchDto);
        return "feed";
    }

    @GetMapping("/events")
    public String events(Authentication authentication, Model model){
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }
        SearchDto searchDto = new SearchDto();
        model.addAttribute("query", searchDto);
        return "feed";
    }

    @GetMapping("/upload")
    public String upload(Model model, Authentication authentication){
        Document document = new Document();
        Post post = new Post();
        model.addAttribute("post", post);
        model.addAttribute("document", document);
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }
        return "upload";
    }

    // shows edit form
    @GetMapping("/edit")
    public String showEditForm(Model model, Authentication authentication){
        // dto stores form data
        Dto user = new Dto();
        model.addAttribute("user", user);
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", userObject(authentication).getId()+".png");}
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
                               @RequestParam("image") MultipartFile file) throws IOException {
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

    @PostMapping("/newPost")
    public String createPost(@Valid @ModelAttribute("post") Post post) {
        if (post != null) {
            postRepository.save(post);
        }
        return "feed";
    }

    @PostMapping("/newDocument")
    public String createDocument(@Valid @ModelAttribute("document") Document document) throws IOException {
        if (document != null) {
            System.out.println("---------------------->"+document.getFilePath());
            cloudService.fileUpload(document.getFilePath());
            documentRepository.save(document);
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
    public String post(Model model, Authentication authentication){
        Post post = new Post();
        model.addAttribute("post", post);
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }
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
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }

        // profile info if no id attached
        model.addAttribute("email", userObject(authentication).getEmail());
        model.addAttribute("description", userObject(authentication).getDescription());
        model.addAttribute("user", userObject(authentication).getName());
        model.addAttribute("age", userObject(authentication).getAge());
        model.addAttribute("location", userObject(authentication).getLocation());


        return "profile";
    }

    @GetMapping("/profile/{id}")
    public String foreignProfile(@PathVariable(value = "id") Long id ,Model model, Authentication authentication){
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }

        // profile info if id attached
        model.addAttribute("email", searchUserObjectId(authentication, id).getEmail());
        model.addAttribute("description", searchUserObjectId(authentication, id).getDescription());
        model.addAttribute("user", searchUserObjectId(authentication, id).getName());
        model.addAttribute("age", searchUserObjectId(authentication, id).getAge());
        model.addAttribute("location", searchUserObjectId(authentication, id).getLocation());
        return "profile";
        }

    @GetMapping("/search")
    public String search(Model model, Authentication authentication, @ModelAttribute("query") SearchDto searchDto) {
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = checkIfLoggedIn(authentication);
        boolean navImg = checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", userObject(authentication).getId() + ".png");
        } catch (Exception e) {
        }
        String[] queries = searchDto.getQuery().split("\\s+");
        Set<User> uniqueUsers = new HashSet<User>();
        for (String query : queries) {
            for (User user : userRepository.findByNameContainingIgnoreCase(query)) {
                uniqueUsers.add(user);
            }
        }
        for (User user : uniqueUsers) {
            System.out.println(user.getName());
        }
        model.addAttribute("uniqueUsers", uniqueUsers);
        return "redirect:/feed?success";
    }
}
