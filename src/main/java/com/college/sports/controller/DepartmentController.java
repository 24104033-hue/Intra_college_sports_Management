package com.college.sports.controller;

import com.college.sports.entity.Event;
import com.college.sports.entity.Player;
import com.college.sports.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String dashboard(@RequestParam String dept, Model model) {
        model.addAttribute("dept", dept);
        return "dept_dashboard";
    }

    @GetMapping("/register")
    public String showRegister(@RequestParam String dept, Model model) {
        model.addAttribute("dept", dept);
        model.addAttribute("events", departmentService.getAvailableEvents());
        model.addAttribute("schedules", departmentService.getScheduleMap());
        return "dept_register";
    }

    @GetMapping("/register/form")
    public String showRegisterForm(@RequestParam String dept, @RequestParam Integer eventId, Model model) {
        Event event = departmentService.getEventById(eventId);
        model.addAttribute("dept", dept);
        model.addAttribute("event", event);
        int numPlayers = "Group".equalsIgnoreCase(event.getType()) ? event.getMaxPlayers() : 1;
        model.addAttribute("numPlayers", numPlayers);
        return "dept_register_form";
    }

    @PostMapping("/register/submit")
    public String register(@RequestParam String dept, @RequestParam Integer eventId,
                           @RequestParam List<String> playerName, 
                           @RequestParam List<String> playerRoll, 
                           @RequestParam List<String> playerYear) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerName.size(); i++) {
            players.add(new Player(null, eventId, dept, playerName.get(i), playerRoll.get(i), playerYear.get(i)));
        }
        departmentService.registerEvent(eventId, dept, players);
        return "redirect:/department?dept=" + dept;
    }

    @GetMapping("/points")
    public String viewPoints(@RequestParam String dept, Model model) {
        model.addAttribute("dept", dept);
        model.addAttribute("points", departmentService.getPoints(dept));
        return "dept_points";
    }

    @GetMapping("/event-details")
    public String viewEventDetails(@RequestParam String dept, Model model) {
        model.addAttribute("dept", dept);
        model.addAttribute("eventDetails", departmentService.getAllEventResultDetails());
        return "dept_event_details";
    }
}
