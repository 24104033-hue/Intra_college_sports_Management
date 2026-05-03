package com.college.sports.repository;

import com.college.sports.entity.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixtureRepository extends JpaRepository<Fixture, Integer> {
    List<Fixture> findByEventId(Integer eventId);
}
