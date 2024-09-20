package com.example.footballchampionship;

import com.example.footballchampionship.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MatchController {

    private final MatchService matchService;
    private final TeamService teamService;
    private final ValidationService validationService;

    public MatchController(MatchService matchService, TeamService teamService, ValidationService validationService) {
        this.matchService = matchService;
        this.teamService = teamService;
        this.validationService = validationService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/matches")
    public ResponseEntity<List<Match>> addMatches(@RequestBody List<Match> matches) {
        List<Team> teams = teamService.getAllTeams();
        List<Match> currentMatches = matchService.getAllMatches();
        try {
            validationService.validateMatches(teams, matches, currentMatches);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add matches");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Match> savedMatches = matchService.addMatches(matches);
        if (savedMatches != null && !savedMatches.isEmpty()) {
            return new ResponseEntity<>(savedMatches, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/editMatches")
    public ResponseEntity<List<Match>> editMatches(@RequestBody List<Match> matches) {
        for (Match inputMatch : matches) {
            boolean matchExists = matchService.existsById(inputMatch.getId());
            if (!matchExists) {
                System.out.println("Failed to add matches");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        List<Match> savedMatches = matchService.updateMatch(matches);
        if (savedMatches != null && !savedMatches.isEmpty()) {
            return new ResponseEntity<>(savedMatches, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/rankings")
    public ResponseEntity<List<TeamScore>> getRankings() {
        List<Match> matches = matchService.getAllMatches();
        List<Team> teams = teamService.getAllTeams();
        List<TeamScore> teamScore = validationService.calculateRankings(matches, teams);
        if (teamScore != null && !teamScore.isEmpty()) {
            return new ResponseEntity<>(teamScore, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value="/api/teams/{teamName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDetails> retrieveInfo(@PathVariable String teamName) {
        try {

            Team team = teamService.getTeam(teamName);
            List<Match> matches = matchService.getAllMatches();
            List<Match> teamMatches = new ArrayList<>();
            for (Match match : matches) {
                if (match.getId().getTeamHomeName().equals(teamName) || match.getId().getTeamAwayName().equals(teamName)) {
                    teamMatches.add(match);
                }
            }
            TeamDetails teamDetails = new TeamDetails(team, teamMatches);
            System.out.println("Team name: " + teamName);
            return new ResponseEntity<>(teamDetails, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}