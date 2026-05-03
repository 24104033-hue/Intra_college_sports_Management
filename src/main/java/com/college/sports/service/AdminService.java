package com.college.sports.service;

import com.college.sports.entity.Captain;
import com.college.sports.entity.Event;
import com.college.sports.repository.CaptainRepository;
import com.college.sports.repository.EventRepository;
import com.college.sports.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final EventRepository eventRepository;
    private final CaptainRepository captainRepository;
    private final PointRepository pointRepository;

    public AdminService(EventRepository eventRepository, CaptainRepository captainRepository, PointRepository pointRepository) {
        this.eventRepository = eventRepository;
        this.captainRepository = captainRepository;
        this.pointRepository = pointRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Captain> getAllCaptains() {
        return captainRepository.findAll();
    }

    public String getFinalResult() {
        return pointRepository.findAll().stream()
                .max((p1, p2) -> p1.getScore().compareTo(p2.getScore()))
                .map(p -> p.getDept())
                .orElse("No Results Yet");
    }
}
