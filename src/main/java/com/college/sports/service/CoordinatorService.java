package com.college.sports.service;

import com.college.sports.entity.*;
import com.college.sports.exception.EventNotFoundException;
import com.college.sports.exception.ResultAlreadyDeclaredException;
import com.college.sports.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoordinatorService {
    private final EventRepository eventRepository;
    private final ScheduleRepository scheduleRepository;
    private final FixtureRepository fixtureRepository;
    private final PointRepository pointRepository;
    private final CaptainRepository captainRepository;
    private final ResultRepository resultRepository;
    private final RegistrationRepository registrationRepository;
    private final PlayerRepository playerRepository;

    public CoordinatorService(EventRepository eventRepository, ScheduleRepository scheduleRepository, FixtureRepository fixtureRepository, PointRepository pointRepository, CaptainRepository captainRepository, ResultRepository resultRepository, RegistrationRepository registrationRepository, PlayerRepository playerRepository) {
        this.eventRepository = eventRepository;
        this.scheduleRepository = scheduleRepository;
        this.fixtureRepository = fixtureRepository;
        this.pointRepository = pointRepository;
        this.captainRepository = captainRepository;
        this.resultRepository = resultRepository;
        this.registrationRepository = registrationRepository;
        this.playerRepository = playerRepository;
    }

    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    public Schedule addSchedule(Schedule schedule) {
        if (!eventRepository.existsById(schedule.getEventId())) {
            throw new EventNotFoundException("Invalid Event ID");
        }
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public List<Fixture> createFixtures(Integer eventId, int numMatches, List<String> dept1s, List<String> dept2s) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Invalid Event"));
        
        if (event.isResultDeclared()) {
            throw new ResultAlreadyDeclaredException("Cannot create fixtures - Result already declared");
        }

        List<Registration> registrations = registrationRepository.findByEventId(eventId);
        List<String> registeredDepts = registrations.stream().map(Registration::getDept).toList();
        
        List<Fixture> createdFixtures = new ArrayList<>();
        for (int i = 0; i < numMatches; i++) {
            String dept1 = dept1s.get(i).toUpperCase();
            String dept2 = dept2s.get(i).toUpperCase();
            
            if (!registeredDepts.contains(dept1) || !registeredDepts.contains(dept2)) {
                throw new RuntimeException("One or both departments are not registered for this event: " + dept1 + ", " + dept2);
            }
            if (dept1.equals(dept2)) {
                throw new RuntimeException("A department cannot play against itself: " + dept1);
            }
            
            String matchDetails = dept1 + " vs " + dept2;
            Fixture fixture = new Fixture(null, eventId, matchDetails);
            createdFixtures.add(fixtureRepository.save(fixture));
        }
        return createdFixtures;
    }

    public java.util.Map<String, Object> getEventDetailedResult(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Invalid Event ID");
        }
        Result result = resultRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Result not declared yet for this event"));
        List<Player> winnerPlayers = playerRepository.findByEventIdAndDept(eventId, result.getWinnerDept().toUpperCase());
        List<Player> runnerPlayers = playerRepository.findByEventIdAndDept(eventId, result.getRunnerDept().toUpperCase());
        java.util.Map<String, Object> details = new java.util.HashMap<>();
        details.put("winnerDept", result.getWinnerDept().toUpperCase());
        details.put("runnerDept", result.getRunnerDept().toUpperCase());
        details.put("winnerPlayers", winnerPlayers);
        details.put("runnerPlayers", runnerPlayers);
        return details;
    }

    public String exportAllEventResultsToCsv() {
        List<Result> results = resultRepository.findAll();
        StringBuilder csv = new StringBuilder();
        csv.append("Event ID,Event Name,Department Role,Department Name,Player Name,Roll No,Year\n");
        
        for (Result result : results) {
            Integer eventId = result.getEventId();
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event == null) continue;

            List<Player> winnerPlayers = playerRepository.findByEventIdAndDept(eventId, result.getWinnerDept().toUpperCase());
            List<Player> runnerPlayers = playerRepository.findByEventIdAndDept(eventId, result.getRunnerDept().toUpperCase());
            
            if (winnerPlayers.isEmpty()) {
                csv.append(eventId).append(",").append(event.getName()).append(",Winner,")
                   .append(result.getWinnerDept().toUpperCase()).append(",No Players found,,,\n");
            } else {
                for (Player p : winnerPlayers) {
                    csv.append(eventId).append(",").append(event.getName()).append(",Winner,")
                       .append(result.getWinnerDept().toUpperCase()).append(",")
                       .append(p.getName()).append(",").append(p.getRollNo()).append(",")
                       .append(p.getYear()).append("\n");
                }
            }
            
            if (runnerPlayers.isEmpty()) {
                csv.append(eventId).append(",").append(event.getName()).append(",Runner,")
                   .append(result.getRunnerDept().toUpperCase()).append(",No Players found,,,\n");
            } else {
                for (Player p : runnerPlayers) {
                    csv.append(eventId).append(",").append(event.getName()).append(",Runner,")
                       .append(result.getRunnerDept().toUpperCase()).append(",")
                       .append(p.getName()).append(",").append(p.getRollNo()).append(",")
                       .append(p.getYear()).append("\n");
                }
            }
        }
        
        return csv.toString();
    }

    public List<Fixture> viewFixtures(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Invalid Event ID");
        }
        return fixtureRepository.findByEventId(eventId);
    }

    @Transactional
    public void addResult(Result result) {
        Event event = eventRepository.findById(result.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Invalid Event ID"));
                
        if (event.isResultDeclared()) {
            throw new ResultAlreadyDeclaredException("Already declared");
        }

        result.setWinnerDept(result.getWinnerDept().toUpperCase());
        result.setRunnerDept(result.getRunnerDept().toUpperCase());

        // Update points
        Point winPoint = pointRepository.findById(result.getWinnerDept()).orElse(new Point(result.getWinnerDept(), 0));
        winPoint.setScore(winPoint.getScore() + event.getWinPts());
        pointRepository.save(winPoint);

        Point runPoint = pointRepository.findById(result.getRunnerDept()).orElse(new Point(result.getRunnerDept(), 0));
        runPoint.setScore(runPoint.getScore() + event.getRunPts());
        pointRepository.save(runPoint);

        event.setResultDeclared(true);
        eventRepository.save(event);
        resultRepository.save(result);
    }

    public List<Point> viewPoints() {
        return pointRepository.findAll();
    }

    public Captain addCaptain(Captain captain) {
        captain.setDept(captain.getDept().toUpperCase());
        if (captainRepository.existsById(captain.getDept())) {
            throw new RuntimeException("Captain already assigned for dept: " + captain.getDept());
        }
        return captainRepository.save(captain);
    }
}
