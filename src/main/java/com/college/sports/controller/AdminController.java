package com.college.sports.controller;

import com.college.sports.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String dashboard() {
        return "admin_dashboard";
    }

    @GetMapping("/events")
    public String viewEvents(Model model) {
        model.addAttribute("events", adminService.getAllEvents());
        return "admin_events";
    }

    @GetMapping("/captains")
    public String viewCaptains(Model model) {
        model.addAttribute("captains", adminService.getAllCaptains());
        return "admin_captains";
    }

    @GetMapping("/result")
    public String viewResult(Model model) {
        model.addAttribute("result", adminService.getFinalResult());
        return "admin_result";
    }
}
