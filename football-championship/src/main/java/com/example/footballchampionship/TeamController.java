package com.example.footballchampionship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TeamController {

    @Autowired
    private MatchService matchService;
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/teams/add")
    public ResponseEntity<List<Team>> addTeams(@RequestBody List<Team> teams) {
        for (Team inputTeam : teams) {
            boolean teamExists = teamService.existsById(inputTeam.getTeamId());
            if (teamExists) {
                System.out.println("Failed to add teams");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        List<Team> savedTeams = teamService.addTeams(teams);
        if (savedTeams != null && !savedTeams.isEmpty()) {
            return new ResponseEntity<>(savedTeams, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/teams/edit")
    public ResponseEntity<List<Team>> editTeams(@RequestBody List<Team> teams) {
        for (Team inputTeam : teams) {
            boolean teamExists = teamService.existsById(inputTeam.getTeamId());
            if (!teamExists) {
                System.out.println("Failed to add teams");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        List<Team> savedTeams = teamService.updateTeams(teams);
        if (savedTeams != null && !savedTeams.isEmpty()) {
            return new ResponseEntity<>(savedTeams, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/clearTeams")
    public ResponseEntity<Void> clearTeams(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        teamService.clearData(email);
        matchService.clearData(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
