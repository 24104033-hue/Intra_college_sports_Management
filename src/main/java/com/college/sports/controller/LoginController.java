package com.college.sports.controller;

import com.college.sports.exception.InvalidLoginException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/")
    public String index() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String role, 
                        @RequestParam(required = false) String username, 
                        @RequestParam String password) {
        if ("ADMIN".equals(role)) {
            if ("admin".equals(username) && "admin123".equals(password)) {
                return "redirect:/admin";
            }
        } else if ("COORDINATOR".equals(role)) {
            if ("coord".equals(username) && "coord123".equals(password)) {
                return "redirect:/coordinator";
            }
        } else if ("DEPARTMENT".equals(role)) {
            // For department, username is the department name
            if (password.equals(username + "123")) {
                return "redirect:/department?dept=" + username.toUpperCase();
            }
        }
        throw new InvalidLoginException("Invalid credentials for " + role);
    }
}
