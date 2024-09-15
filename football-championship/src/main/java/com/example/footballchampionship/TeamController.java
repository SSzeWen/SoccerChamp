package com.example.footballchampionship;

import com.example.footballchampionship.Team;
import com.example.footballchampionship.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/teams")
    public ResponseEntity<List<Team>> addTeams(@RequestBody List<Team> teams) {
        List<Team> savedTeams = teamService.addTeams(teams);
        if (savedTeams != null && !savedTeams.isEmpty()) {
            return new ResponseEntity<>(savedTeams, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
