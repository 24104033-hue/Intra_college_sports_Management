package com.college.sports.service;

import com.college.sports.entity.Point;
import com.college.sports.repository.PointRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService {
    private final PointRepository pointRepository;
    private final String[] depts = {"CSE", "IT", "AIDS", "ECE", "EEE", "CIVIL", "MECH", "S&H"};

    public DataInitializationService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @PostConstruct
    public void init() {
        for (String dept : depts) {
            if (!pointRepository.existsById(dept)) {
                pointRepository.save(new Point(dept, 0));
            }
        }
    }
}
