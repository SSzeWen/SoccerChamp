package com.example.footballchampionship;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, TeamId> {
    @Transactional
    void deleteByTeamId_Email(String email);
    List<Team> findByTeamId_Email(String email);
}
