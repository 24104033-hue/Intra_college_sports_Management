package com.college.sports.repository;

import com.college.sports.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findByEventIdAndDept(Integer eventId, String dept);
}
