package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {


    // dosent need permissions
    @RequestMapping("/index")
    public String main() {
        return "main";}


    // page that needs permission to access
    @RequestMapping("/needlogin")
    public String proof() {
        return "show";
}
}
