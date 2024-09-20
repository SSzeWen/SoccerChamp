package com.example.footballchampionship;

import com.example.footballchampionship.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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

    public Team getTeam(String name) throws ResourceNotFoundException {
        return teamRepository.findById(name).orElseThrow(() -> new ResourceNotFoundException("Team not found"));
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Team> updateTeams(List<Team> teams) {
        return teamRepository.saveAll(teams);
    }

    boolean existsByName(String name) {
        return teamRepository.existsById(name);
    }

    public void clearData() {
        teamRepository.deleteAll();
    }
}
