package com.stayfeelen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    public HomeController() {
        
    }
    
    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }
  
}
