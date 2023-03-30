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

    //FeedController handles requests relating to Feed

    //importing the relevant repositories to handle Events
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;
    @Autowired
    private FeedRepository feedRepo;

    // Handles GET requests at '/post', when /post is accessed, this request mapping returns the Post creation form
    @GetMapping("/post")
    public String post(Model model, Authentication authentication){
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);
        boolean navImg = searchAndIdentifyService.checkImg(authentication);
        model.addAttribute("navImg", navImg);model.addAttribute("navUser", navUser);
        try {
            model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");
        }
        catch(Exception e) {}
         //imports navbar image - checks if user is logged in^

        return "feed/post";
         //returns page to create a new post
    }

    //Handles POST requests at '/createPost', after a user submits a request to create a Post, this request mapping handles the submission
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
        //Validates title and text, makes sure not empty, also validates the length

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        //Gets the date/time the post is submitted

        //Creates a post object, assigns the relevant attributes to the post
        //Then saves the post to the database through the postRepository
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setAuthor(searchAndIdentifyService.userObject(authentication).getName());
        post.setPostDate(dtf.format(now));
        postRepo.save(post);

        //Creates a feed object, assigns the relevant attributes to the feed
        //Then saves the feed to the database through the FeedRepository
        //we use both FeedRepo and PostRepo, because we want Posts to show in feed too
        Feed feed = new Feed();
        feed.setTitle(title);
        feed.setText(text);
        feed.setAuthor(searchAndIdentifyService.userObject(authentication).getName());
        feed.setAuthorId(searchAndIdentifyService.userObject(authentication).getId());
        feed.setPostDate(dtf.format(now));
        feedRepo.save(feed);

        return "redirect:/feed";
    }

    // Handles GET requests at '/feed', when /feed is accessed, this request mapping returns the feed page
    @GetMapping("/feed")
    public String viewFeed(Model model, Authentication authentication){
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);boolean navImg = searchAndIdentifyService.checkImg(authentication);model.addAttribute("navImg", navImg);model.addAttribute("navUser", navUser);try {model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");} catch(Exception e) {}
         //imports navbar image - checks if user is logged in^
           
        List<Feed> posts = null;
        posts = feedRepo.findAll();
        //gets all the feed objects
        Collections.sort(posts, (new DateSortService()).reversed());
        //Sorts the feed in reverse-chronological order
        model.addAttribute("posts", posts);
        //adds the feed objects to the page as a model attribute
        return "feed/feed";
        //returns the feed page (feed.html)
    }
}
