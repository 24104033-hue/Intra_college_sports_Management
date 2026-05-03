package com.college.sports.controller;

import com.college.sports.entity.*;
import com.college.sports.service.CoordinatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Controller
@RequestMapping("/coordinator")
public class CoordinatorController {
    private final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @GetMapping
    public String dashboard() {
        return "coord_dashboard";
    }

    @GetMapping("/event/add")
    public String showAddEvent() {
        return "coord_add_event";
    }

    @PostMapping("/event/add")
    public String addEvent(Event event) {
        coordinatorService.addEvent(event);
        return "redirect:/coordinator";
    }

    @GetMapping("/schedule/add")
    public String showAddSchedule() {
        return "coord_add_schedule";
    }

    @PostMapping("/schedule/add")
    public String addSchedule(Schedule schedule, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            coordinatorService.addSchedule(schedule);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/coordinator/schedule/add";
        }
        return "redirect:/coordinator";
    }

    @GetMapping("/fixtures/create")
    public String showCreateFixtures() {
        return "coord_create_fixtures";
    }

    @PostMapping("/fixtures/create")
    public String createFixtures(@RequestParam Integer eventId, @RequestParam int numMatches, 
                                 @RequestParam List<String> dept1, @RequestParam List<String> dept2,
                                 org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            coordinatorService.createFixtures(eventId, numMatches, dept1, dept2);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/coordinator/fixtures/create";
        }
        return "redirect:/coordinator";
    }

    @GetMapping("/fixtures/view")
    public String viewFixtures(@RequestParam(required = false) Integer eventId, Model model) {
        if (eventId != null) {
            model.addAttribute("fixtures", coordinatorService.viewFixtures(eventId));
        }
        return "coord_view_fixtures";
    }

    @GetMapping("/result/add")
    public String showAddResult() {
        return "coord_add_result";
    }

    @PostMapping("/result/add")
    public String addResult(Result result) {
        coordinatorService.addResult(result);
        return "redirect:/coordinator";
    }

    @GetMapping("/event/result")
    public String viewEventDetailedResult(@RequestParam(required = false) Integer eventId, Model model) {
        if (eventId != null) {
            try {
                model.addAttribute("resultDetails", coordinatorService.getEventDetailedResult(eventId));
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "coord_event_result";
    }

    @GetMapping(value = "/event/result/export", produces = "text/csv")
    public ResponseEntity<String> exportAllEventResults() {
        try {
            String csvData = coordinatorService.exportAllEventResultsToCsv();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"all_event_results.csv\"");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/points")
    public String viewPoints(Model model) {
        model.addAttribute("points", coordinatorService.viewPoints());
        return "coord_points";
    }

    @GetMapping("/captain/add")
    public String showAddCaptain() {
        return "coord_add_captain";
    }

    @PostMapping("/captain/add")
    public String addCaptain(Captain captain) {
        coordinatorService.addCaptain(captain);
        return "redirect:/coordinator";
    }
}
