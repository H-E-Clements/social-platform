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

    //EditController handles requests relating to user's editing their information

    //importing the relevant repositories to handle information editing
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

    //UPLOAD_DIRECTORY stores the directory under which profile pictures are to be stored
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/pfp";

    // Handles GET requests at '/edit', when /edit is accessed, this request mapping returns the edit form
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

        //returns edit.html
        return "edit";
    }

    //Handles POST requests at '/edit/save/{param}', after a user submits a request to edit their informatione, this request mapping handles the submission
    @PostMapping("/edit/save/{param}")
    public String edit(@Valid @ModelAttribute("user") @PathVariable(value = "param") String param,
                       Dto userDto,
                       BindingResult result,
                       Model model,
                       Authentication authentication,
                       @RequestParam(name="image", required = false) MultipartFile file) throws IOException {

        //First gets the user's current information via the repository
        User existingUser = userService.findUserByEmail(userDto.getEmail());
        String currentUser = authentication.getName();
        User user = userRepository.findByEmail(currentUser);

        //param checking - checks which information the user wants to edit
        //in this case, if the user updates their email
        if (param.equals("email")) {
            
            //Checks if new email aready exists
            if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
                result.rejectValue("email", null,
                        "There is already an account registered with the same email");
            }

            //Checks against other errors when submitting an email
            if(result.hasErrors()){
                model.addAttribute("user", userDto);
                return "/edit";
            }

            //If no errors, stores the new email to the DB and logs the user out (because they've changed their email)
            user.setEmail(userDto.getEmail());
            userRepository.save(user);
            return "redirect:/logout";
        }

        //If user wants to change their name
        else if (param.equals("name")) {
            //Assigns new first name and last name and redirects them back to profile
            user.setName(userDto.getFirstName() + " " + userDto.getLastName());
            userRepository.save(user);
            return "redirect:/profile";
        }

        //If user wants to change their password
        else if (param.equals("password")) {
            //Encodes/encrypts the submitted password and saves the password, then logs the user out (because they've changed their password successfully)
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
            return "redirect:/logout";
        }

        //If user wants to change their description/bio
        else if (param.equals("description")) {
            //Assigns new description, saves to DB and redirects user to their profile
            user.setDescription(userDto.getDescription());
            userRepository.save(user);
            return "redirect:/profile";
        }

        //If user wants to change their age
        else if (param.equals("age")) {
            //Assings and saves new age, redirects back to profile
            user.setAge(userDto.getAge());
            userRepository.save(user);
            return "redirect:/profile";
        }

        //If user wants to change location
        else if (param.equals("location")) {
            //Assigns and saves new location, redirects back to profile
            user.setLocation(userDto.getLocation());
            userRepository.save(user);
            return "redirect:/profile";
        }

        //If user wants to upload a profile picture
        else if (param.equals("image")) {
            //The path of the uploaded PFP is the PFP directory, then the user's ID, then the extension (.png)
            //So a PFP is linked to a user via the user's ID
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, Long.toString(user.getId())+".png");
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
            //Sets the image attribute of the user to true (meaning they now have a pfp)
            //Then save the new information to DB and redirect back to edit page
            user.setImg(true);
            userRepository.save(user);
            return "redirect:/edit";
        }

        //If user wants to remove their PFP
        else if (param.equals("remove")) {
            //Sets the image attribute of the user to false (meaning they don't have a PFP)
            //Saves changes and redirects to profile page
            user.setImg(false);
            userRepository.save(user);
            return "redirect:/profile";
        }

        //If edits are successful, lets the page know using '?success'
        return "redirect:/edit?success";
    }
}
