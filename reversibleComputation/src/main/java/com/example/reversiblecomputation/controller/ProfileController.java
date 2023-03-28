package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.Comment;
import com.example.reversiblecomputation.domain.Feed;
import com.example.reversiblecomputation.domain.User;
import com.example.reversiblecomputation.dto.CommentDto;
import com.example.reversiblecomputation.dto.SearchDto;
import com.example.reversiblecomputation.repository.CommentRepository;
import com.example.reversiblecomputation.repository.CrudUserRepository;
import com.example.reversiblecomputation.repository.FeedRepository;
import com.example.reversiblecomputation.repository.UserRepository;
import com.example.reversiblecomputation.service.CloudService;
import com.example.reversiblecomputation.service.DateSortService;
import com.example.reversiblecomputation.service.SearchAndIdentifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Collections;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    CloudService cloudService;

    @Autowired
    private FeedRepository feedRepo;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @GetMapping("/profile")
    public String profile(@RequestParam(required = false) String page, Model model, Authentication authentication){
        // dto for search bar
        SearchDto searchDto = new SearchDto();
        model.addAttribute("query", searchDto);

        model.addAttribute("userPage", 0);
        model.addAttribute("userObj", searchAndIdentifyService.userObject(authentication));
        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId() + ".png");}
        catch(Exception e) {
        }

        // profile info if no id attached
        model.addAttribute("email", searchAndIdentifyService.userObject(authentication).getEmail());
        model.addAttribute("description", searchAndIdentifyService.userObject(authentication).getDescription());
        model.addAttribute("user", searchAndIdentifyService.userObject(authentication).getName());
        model.addAttribute("age", searchAndIdentifyService.userObject(authentication).getAge());
        model.addAttribute("location", searchAndIdentifyService.userObject(authentication).getLocation());

        // for viewing posts
        List<Feed> posts = null;
        posts = feedRepo.findAllByAuthor(searchAndIdentifyService.userObject(authentication).getName());
        Collections.sort(posts, (new DateSortService()).reversed());
        model.addAttribute("posts", posts);

        // dto for search bar
        CommentDto commentDto = new CommentDto();
        model.addAttribute("commentDto", commentDto);


        // comments
        List<Comment> comments = null;
        comments = commentRepository.findAllByTarget(searchAndIdentifyService.userObject(authentication));
        Collections.sort(posts, (new DateSortService()).reversed());
        model.addAttribute("comments", comments);
        model.addAttribute("searchAndIdentify", searchAndIdentifyService);
        System.out.println(comments);


        if (page != null) {
            if (page.equals("posts")) {
                model.addAttribute("page", "posts");
                return "profile";
            }
            else if (page.equals("comments")) {
                model.addAttribute("page", "comments");
                return "profile";
            }
        }
        model.addAttribute("page", "description");
        return "profile";
    }


    @GetMapping("/profile/{id}")
    public String foreignProfile(@PathVariable(value = "id", required = false) Long id, @RequestParam(required = false) String page, Model model, Authentication authentication) {
        // dto for search bar
        SearchDto searchDto = new SearchDto();
        model.addAttribute("query", searchDto);
        model.addAttribute("userObj", searchAndIdentifyService.searchUserObjectId(authentication, id));
        // dto for comment
        model.addAttribute("commentDto2", new CommentDto());
        model.addAttribute("userPage", id);

        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);boolean navImg = searchAndIdentifyService.checkImg(authentication);model.addAttribute("navImg", navImg);model.addAttribute("navUser", navUser);try {model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId() + ".png");} catch (Exception e) {}

        // profile info if id attached
        model.addAttribute("email", searchAndIdentifyService.searchUserObjectId(authentication, id).getEmail());
        model.addAttribute("description", searchAndIdentifyService.searchUserObjectId(authentication, id).getDescription());
        model.addAttribute("user", searchAndIdentifyService.searchUserObjectId(authentication, id).getName());
        model.addAttribute("age", searchAndIdentifyService.searchUserObjectId(authentication, id).getAge());
        model.addAttribute("location", searchAndIdentifyService.searchUserObjectId(authentication, id).getLocation());
        model.addAttribute("userRepository", userRepository);

        // for viewing posts
        List<Feed> posts = null;
        posts = feedRepo.findAllByAuthor(searchAndIdentifyService.searchUserObjectId(authentication, id).getName());
        Collections.sort(posts, (new DateSortService()).reversed());
        model.addAttribute("posts", posts);
        // comments
        List<Comment> comments = null;
        comments = commentRepository.findAllByTarget(searchAndIdentifyService.searchUserObjectId(authentication, id));
        model.addAttribute("comments", comments);
        model.addAttribute("searchAndIdentify", searchAndIdentifyService);

        User searchedUser = crudUserRepository.findById(id).get();
        searchedUser.setViews(searchedUser.getViews()+1);
        crudUserRepository.save(searchedUser);


        if (page != null) {
            if (page.equals("posts")) {
                model.addAttribute("page", "posts");
                return "profile";
            } else if (page.equals("comments")) {
                model.addAttribute("page", "comments");
                return "profile";
            }
        }
            model.addAttribute("page", "description");
            return "profile";


    }
}