package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.Comment;
import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.dto.CommentDto;
import com.example.reversiblecomputation.repository.CommentRepository;
import com.example.reversiblecomputation.repository.UserRepository;
import com.example.reversiblecomputation.service.SearchAndIdentifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class CommentController {

    //CommentController handles requests relating to comments (comments on a users profile)

    //importing the relevant repositories to handle comments
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;
    @Autowired
    private UserRepository userRepository;

    //Handles POST requests at '/createComment', after a user submits a comment to their own profile, this request mapping handles the submission
    @PostMapping("/createComment")
    private String createComment(Authentication authentication, @ModelAttribute("commentDto") CommentDto commentDto){
        //Validates title and text, makes sure not empty

        //This PostMapping handles requests when a user comments on their own profile

        //Gets the date/time the comment is submitted
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        //Creates a comment object, assigns the relevant attributes to the comment
        //Then saves the comment to the database through the commentRepository
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUser(searchAndIdentifyService.userObject(authentication));
        comment.setDate(dtf.format(now));
        comment.setTarget(searchAndIdentifyService.userObject(authentication));
        commentRepository.save(comment);

        //Redirects back to the profile page after comment submission is handled
        return "redirect:/profile";
    }

    //Handles POST requests at '/createComment/{id}', after a user submits a comment on some else's profile, this request mapping handles the submission
    @PostMapping("/createComment/{id}")
    private String createComment_Spec(@PathVariable(value = "id", required = false) Long id, @ModelAttribute("commentDto") CommentDto commentDto2, Authentication authentication){
        //Validates title and text, makes sure not empty

        //This PostMapping handles requests when a user comments on someone else's profile (indicated by the {id} parameter being passed in via URL)

        //Gets the date/time the comment is submitted
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        //Creates a comment object, assigns the relevant attributes to the comment
        //Then saves the comment to the database through the commentRepository
        Comment comment = new Comment();
        comment.setContent(commentDto2.getContent());
        comment.setUser(searchAndIdentifyService.userObject(authentication));
        comment.setDate(dtf.format(now));
        comment.setTarget(searchAndIdentifyService.searchUserObjectId(authentication, id));
        commentRepository.save(comment);

        //Redirects back to the profile page after comment submission is handled
        return "redirect:/profile/" + id;
    }

}
