package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.Feed;
import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.repository.FeedRepository;
import com.example.reversiblecomputation.repository.PostRepository;
import com.example.reversiblecomputation.service.DateSortService;
import com.example.reversiblecomputation.service.SearchAndIdentifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
public class FeedController {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;

    @Autowired
    private FeedRepository feedRepo;

    @GetMapping("/post")
    public String post(Model model, Authentication authentication){
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");
        }
        catch(Exception e) {}
        return "feed/post";
    }

    @PostMapping("/createPost")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("text") String text,
                             Authentication authentication){
        for(String str:title.split(" ")){if (str.length()>55){return "redirect:/post?lenErr";}}
        for(String str:text.split(" ")){if (str.length()>55){return "redirect:/post?lenErr";}}
        if(title.isEmpty()){
            return "redirect:/post?titleErr";
        }
        if(text.isEmpty()){
            return "redirect:/post?textErr";
        }
        //Validates title and text, makes sure not empty

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setAuthor(searchAndIdentifyService.userObject(authentication).getName());
        post.setPostDate(dtf.format(now));
        postRepo.save(post);

        Feed feed = new Feed();
        feed.setTitle(title);
        feed.setText(text);
        feed.setAuthor(searchAndIdentifyService.userObject(authentication).getName());
        feed.setAuthorId(searchAndIdentifyService.userObject(authentication).getId());
        feed.setPostDate(dtf.format(now));
        feedRepo.save(feed);

        return "redirect:/feed";
    }

    @GetMapping("/feed")
    public String viewFeed(Model model, Authentication authentication){
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);boolean navImg = searchAndIdentifyService.checkImg(authentication);model.addAttribute("navImg", navImg);model.addAttribute("navUser", navUser);try {model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");} catch(Exception e) {}
        List<Feed> posts = null;
        posts = feedRepo.findAll();
        Collections.sort(posts, (new DateSortService()).reversed());
        model.addAttribute("posts", posts);
        return "feed/feed";
    }
}
