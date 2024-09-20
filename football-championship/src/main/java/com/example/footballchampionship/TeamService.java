package com.example.footballchampionship;

import com.example.footballchampionship.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Transactional
    public List<Team> addTeams(List<Team> teams) {
        return teamRepository.saveAll(teams);
    }

    public Team getTeam(TeamId teamId) throws ResourceNotFoundException {
        return teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team not found"));
    }

    public List<Team> getAllTeams(String email) {
        return teamRepository.findByTeamId_Email(email);
    }

    public List<Team> updateTeams(List<Team> teams) {
        return teamRepository.saveAll(teams);
    }

    boolean existsById(TeamId teamId) {
        return teamRepository.existsById(teamId);
    }

    public void clearData(String email) {
        teamRepository.deleteByTeamId_Email(email);
    }
}
