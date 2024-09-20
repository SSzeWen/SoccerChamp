package com.example.footballchampionship;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, MatchId> {
    @Transactional
    void deleteByMatchId_Email(String email);
    List<Match> findByMatchId_Email(String email);
}