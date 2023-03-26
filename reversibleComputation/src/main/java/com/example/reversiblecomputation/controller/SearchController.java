package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.Comment;
import com.example.reversiblecomputation.domain.Feed;
import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.domain.User;
import com.example.reversiblecomputation.dto.CommentDto;
import com.example.reversiblecomputation.dto.SearchDto;
import com.example.reversiblecomputation.repository.CommentRepository;
import com.example.reversiblecomputation.repository.FeedRepository;
import com.example.reversiblecomputation.repository.UserRepository;
import com.example.reversiblecomputation.service.DateSortService;
import com.example.reversiblecomputation.service.SearchAndIdentifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SearchController {

    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedRepository feedRepo;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/search/{id}")
    public String search(@PathVariable(value = "id", required = false) Long id, Model model, Authentication authentication, @ModelAttribute("query") SearchDto searchDto) {
        if (id == (0)) {
            model.addAttribute("userObj", searchAndIdentifyService.userObject(authentication));
        }

        else {
        model.addAttribute("userObj", searchAndIdentifyService.searchUserObjectId(authentication, id));
        }

        // checks if logged in and if a pfp is set for nav bar
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);
        model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId() + ".png");
        } catch (Exception e) {
        }
        //splits up string into individual queries
        String[] queries = searchDto.getQuery().split("\\s+");
        Set<User> uniqueUsers = new HashSet<>();
        //looks for each query in database (only user names)
        for (String query : queries) {
            uniqueUsers.addAll(userRepository.findByNameContainingIgnoreCase(query));
        }
        model.addAttribute("uniqueUsers", uniqueUsers);
        model.addAttribute("commentDto2", new CommentDto());
        model.addAttribute("userPage", id);

        if (id > 0) {
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
            CommentDto commentDto = new CommentDto();
            model.addAttribute("commentDto", commentDto);
        List<Comment> comments = null;
        comments = commentRepository.findAllByTarget(searchAndIdentifyService.searchUserObjectId(authentication, id));
        model.addAttribute("comments", comments);
        }
        else {
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
        }

        model.addAttribute("page", "description");
        return "profile";
    }

}
