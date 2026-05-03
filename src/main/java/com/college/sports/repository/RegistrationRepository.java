package com.college.sports.repository;

import com.college.sports.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    List<Registration> findByEventId(Integer eventId);
    Optional<Registration> findByEventIdAndDept(Integer eventId, String dept);
    boolean existsByEventIdAndDept(Integer eventId, String dept);
}
