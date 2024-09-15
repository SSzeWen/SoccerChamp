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
        return teamRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Team not found"));
    }

    public void updateTeam(Team team) throws ResourceNotFoundException {
        Team existingTeam = teamRepository.findByName(team.getName()).orElseThrow(
                () -> new ResourceNotFoundException("Team not found")
        );
        existingTeam.setName(team.getName());
        existingTeam.setRegistrationDate(team.getRegistrationDate());
        existingTeam.setGroupNumber(team.getGroupNumber());
        teamRepository.save(existingTeam);
    }

    public void clearData() {
        teamRepository.deleteAll();
    }
}
