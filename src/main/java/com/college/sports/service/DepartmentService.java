package com.college.sports.service;

import com.college.sports.entity.Event;
import com.college.sports.entity.Player;
import com.college.sports.entity.Registration;
import com.college.sports.exception.AlreadyRegisteredException;
import com.college.sports.exception.EventNotFoundException;
import com.college.sports.exception.ResultAlreadyDeclaredException;
import com.college.sports.repository.EventRepository;
import com.college.sports.repository.PlayerRepository;
import com.college.sports.repository.PointRepository;
import com.college.sports.repository.RegistrationRepository;
import com.college.sports.repository.ScheduleRepository;
import com.college.sports.repository.ResultRepository;
import com.college.sports.entity.Schedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final PlayerRepository playerRepository;
    private final PointRepository pointRepository;
    private final ScheduleRepository scheduleRepository;
    private final ResultRepository resultRepository;

    public DepartmentService(EventRepository eventRepository, RegistrationRepository registrationRepository, PlayerRepository playerRepository, PointRepository pointRepository, ScheduleRepository scheduleRepository, ResultRepository resultRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.playerRepository = playerRepository;
        this.pointRepository = pointRepository;
        this.scheduleRepository = scheduleRepository;
        this.resultRepository = resultRepository;
    }

    public List<Event> getAvailableEvents() {
        List<Integer> scheduledEventIds = scheduleRepository.findAll().stream().map(Schedule::getEventId).toList();
        return eventRepository.findAll().stream()
                .filter(e -> !e.isResultDeclared() && scheduledEventIds.contains(e.getId()))
                .toList();
    }
    
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
    
    public Map<Integer, Schedule> getScheduleMap() {
        return scheduleRepository.findAll().stream().collect(Collectors.toMap(Schedule::getEventId, s -> s));
    }

    @Transactional
    public void registerEvent(Integer eventId, String dept, List<Player> players) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Invalid Event"));
                
        if (event.isResultDeclared()) {
            throw new ResultAlreadyDeclaredException("Result already declared for this event");
        }

        if (registrationRepository.existsByEventIdAndDept(eventId, dept)) {
            throw new AlreadyRegisteredException("Already registered");
        }

        registrationRepository.save(new Registration(null, eventId, dept));
        
        for (Player player : players) {
            player.setEventId(eventId);
            player.setDept(dept);
            playerRepository.save(player);
        }
    }

    public Integer getPoints(String dept) {
        return pointRepository.findById(dept).map(p -> p.getScore()).orElse(0);
    }
    
    public Event getEventById(Integer id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Invalid Event"));
    }

    public List<Map<String, Object>> getAllEventResultDetails() {
        List<Map<String, Object>> detailsList = new java.util.ArrayList<>();
        List<com.college.sports.entity.Result> results = resultRepository.findAll();
        for (com.college.sports.entity.Result result : results) {
            Integer eventId = result.getEventId();
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event == null) continue;

            List<Player> winnerPlayers = playerRepository.findByEventIdAndDept(eventId, result.getWinnerDept().toUpperCase());
            List<Player> runnerPlayers = playerRepository.findByEventIdAndDept(eventId, result.getRunnerDept().toUpperCase());
            
            if (winnerPlayers.isEmpty()) {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("eventId", eventId);
                map.put("eventName", event.getName());
                map.put("deptRole", "Winner");
                map.put("deptName", result.getWinnerDept().toUpperCase());
                map.put("playerName", "No Players found");
                map.put("rollNo", "");
                map.put("year", "");
                detailsList.add(map);
            } else {
                for (Player p : winnerPlayers) {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("eventId", eventId);
                    map.put("eventName", event.getName());
                    map.put("deptRole", "Winner");
                    map.put("deptName", result.getWinnerDept().toUpperCase());
                    map.put("playerName", p.getName());
                    map.put("rollNo", p.getRollNo());
                    map.put("year", p.getYear());
                    detailsList.add(map);
                }
            }

            if (runnerPlayers.isEmpty()) {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("eventId", eventId);
                map.put("eventName", event.getName());
                map.put("deptRole", "Runner");
                map.put("deptName", result.getRunnerDept().toUpperCase());
                map.put("playerName", "No Players found");
                map.put("rollNo", "");
                map.put("year", "");
                detailsList.add(map);
            } else {
                for (Player p : runnerPlayers) {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("eventId", eventId);
                    map.put("eventName", event.getName());
                    map.put("deptRole", "Runner");
                    map.put("deptName", result.getRunnerDept().toUpperCase());
                    map.put("playerName", p.getName());
                    map.put("rollNo", p.getRollNo());
                    map.put("year", p.getYear());
                    detailsList.add(map);
                }
            }
        }
        return detailsList;
    }
}
