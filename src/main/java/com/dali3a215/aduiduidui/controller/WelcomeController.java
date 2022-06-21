package com.dali3a215.aduiduidui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
    @GetMapping("/")
    public String welcome() {
        return "redirect:/index.html";
    }

    @GetMapping("/admin")
    public String welcomeAdmin() {
        return "redirect:/admin/index.html";
    }
}
