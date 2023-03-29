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

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createComment")
    private String createComment(Authentication authentication, @ModelAttribute("commentDto") CommentDto commentDto){
        //Validates title and text, makes sure not empty


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUser(searchAndIdentifyService.userObject(authentication));
        comment.setDate(dtf.format(now));
        comment.setTarget(searchAndIdentifyService.userObject(authentication));
        commentRepository.save(comment);

        return "redirect:/profile";
    }

    @PostMapping("/createComment/{id}")
    private String createComment_Spec(@PathVariable(value = "id", required = false) Long id, @ModelAttribute("commentDto") CommentDto commentDto2, Authentication authentication){
        //Validates title and text, makes sure not empty


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Comment comment = new Comment();
        comment.setContent(commentDto2.getContent());
        comment.setUser(searchAndIdentifyService.userObject(authentication));
        comment.setDate(dtf.format(now));
        comment.setTarget(searchAndIdentifyService.searchUserObjectId(authentication, id));
        commentRepository.save(comment);

        return "redirect:/profile/" + id;
    }

}
