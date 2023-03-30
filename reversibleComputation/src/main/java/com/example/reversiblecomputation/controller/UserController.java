package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.User;
import com.example.reversiblecomputation.repository.CrudUserRepository;
import com.example.reversiblecomputation.repository.UserRepository;
import com.example.reversiblecomputation.service.SearchAndIdentifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class UserController {

    //relevant repos imported
    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;
    @Autowired
    private CrudUserRepository userRepo;

    @GetMapping("/users")
    public String users(Model model, Authentication authentication, String keyword, String searchType){
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");
        }
        catch(Exception e) {}
        //checks navbar image and if logged in

        List<User> users = null;
        //creates a list of users

        if (keyword != null){
            if (searchType.equals("name")){
                users = userRepo.findByKeywordName(keyword);
            }
            if (searchType.equals("location")){
                users = userRepo.findByKeywordLocation(keyword);
            }
            model.addAttribute("users", users);
        }
        //User search feature^, lets people search through users

        else {
            users = userRepo.findAll();
            model.addAttribute("users", users);
        }
        //Gets all users from the repo

        List<User> populars = null;
        populars = userRepo.findAll();;
        Collections.sort(populars, new Comparator<User>() {@Override public int compare(User o1, User o2) {return o2.getViews().compareTo(o1.getViews());}});
        //Sort users by popularity
        int popSize = populars.size();
        if (popSize == 0) {
            populars.clear();
        }
        else if (popSize == 1) {
            User mostPopular = populars.get(0);
            populars.clear();
            populars.add(mostPopular);
        }
        else if (popSize >= 2) {
            User mostPopular = populars.get(0);
            User secondMostPopular = populars.get(1);
            populars.clear();
            populars.add(mostPopular);
            populars.add(secondMostPopular);

        }
        //Get 2 most popular users
        model.addAttribute("populars", populars);
        //Adds the popular users to the page as an attribute
        return "users";
        //returns users page
    }
}
