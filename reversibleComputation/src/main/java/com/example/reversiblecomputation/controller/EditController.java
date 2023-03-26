package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.User;
import com.example.reversiblecomputation.dto.Dto;
import com.example.reversiblecomputation.repository.FeedRepository;
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


@Controller
public class EditController {

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
}
