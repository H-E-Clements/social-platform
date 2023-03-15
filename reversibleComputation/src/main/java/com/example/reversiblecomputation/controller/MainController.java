package com.example.reversiblecomputation.controller;

import autovalue.shaded.com.google.common.base.Charsets;
import autovalue.shaded.com.google.common.hash.Hashing;
import com.example.reversiblecomputation.domain.*;
import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.dto.Dto;
import com.example.reversiblecomputation.dto.SearchDto;
import com.example.reversiblecomputation.repository.DocumentRepository;
import com.example.reversiblecomputation.repository.PostRepository;
import com.example.reversiblecomputation.repository.UserRepository;
import com.example.reversiblecomputation.service.*;
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
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
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

    // feed page
    @GetMapping("/feed")
    public String feed(Authentication authentication, Model model){
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");}
        catch(Exception e) {
        }

        // dto for search bar
        SearchDto searchDto = new SearchDto();
        model.addAttribute("query", searchDto);

        // for viewing posts
        List<Post> posts = postService.findAllElements();
        System.out.println(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("userRepository", userRepository);
        return "feed";
    }

    @GetMapping("/upload")
    public String upload(Model model, Authentication authentication){
        PackagedDocument document = new PackagedDocument();
        Post post = new Post();
        model.addAttribute("post", post);
        model.addAttribute("document", document);
        model.addAttribute("name", "");
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");}
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

        else if (param.equals("remove")) {
            user.setImg(false);
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
    public String createDocument(@ModelAttribute Post post, @RequestParam MultipartFile file, @RequestParam String fileName, Authentication authentication, Model model) {
        try {
            Document document = new Document();
            System.out.println(file.isEmpty());
            if (!file.isEmpty()) {
                if (file.getSize() > 0) {
                    System.out.println("---------------------->"+file.getName());
                    System.out.println("---------------------->"+file.getSize());
                    int lastSlash = fileName.lastIndexOf("\\");
                    int extentionIndex = fileName.lastIndexOf(".");
                    document.setName(fileName.substring(lastSlash+1, extentionIndex));
                    document.setExtension(fileName.substring(extentionIndex));
                    UUID randomId = UUID.randomUUID();
                    document.setId(randomId.toString());

                    cloudService.fileUpload(file.getBytes(), randomId.toString());
    //            documentRepository.save(document);
                }
            }
            else {
                document.setName("EMPTY_FILE");
                document.setExtension("EMPTY_FILE");
                UUID randomId = UUID.randomUUID();
                document.setId(randomId.toString());
                cloudService.fileUpload(file.getBytes(), randomId.toString());
            }
            System.out.println("documentsSize--------------------->"+post.getDocuments().size());
            post.setUploaddate(new Date(System.currentTimeMillis()));
            post.setUser(userRepository.findByEmail(authentication.getName()));
            postRepository.save(post);
            document.setPost(post);
            post.addDocument(document);
            documentRepository.save(document);
        } catch (IOException e) {
            System.err.println(e);
        }

        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);

        return "upload";}


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
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");}
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

        // for viewing posts
        List<Post> posts = postRepository.findAllByUser(searchAndIdentifyService.userObject(authentication));
        System.out.println(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("userRepository", userRepository);

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

        // for viewing posts
        List<Post> posts = postRepository.findAllByUser(searchAndIdentifyService.searchUserObjectId(authentication, id));
        System.out.println(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("userRepository", userRepository);

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
        List<Post> posts = postService.findAllElements();
        model.addAttribute("posts", posts);
        model.addAttribute("userRepository", userRepository);
        model.addAttribute("uniqueUsers", uniqueUsers);
        return "feed";
    }
}
