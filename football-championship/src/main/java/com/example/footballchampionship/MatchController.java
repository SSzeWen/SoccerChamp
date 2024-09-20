package com.example.footballchampionship;

import com.example.footballchampionship.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        String email = matches.get(0).getMatchId().getEmail();
        if (email == null || email.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Team> teams = teamService.getAllTeams(email);
        List<Match> currentMatches = matchService.getAllMatches(email);
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
            boolean matchExists = matchService.existsById(inputMatch.getMatchId());
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
    @PostMapping("/api/rankings")
    public ResponseEntity<List<TeamScore>> getRankings(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        List<Match> matches = matchService.getAllMatches(email);
        List<Team> teams = teamService.getAllTeams(email);
        List<TeamScore> teamScore = validationService.calculateRankings(matches, teams);
        if (teamScore != null && !teamScore.isEmpty()) {
            return new ResponseEntity<>(teamScore, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/retrieveInfo")
    public ResponseEntity<TeamDetails> retrieveInfo(@RequestBody TeamId teamId) {
        try {
            System.out.println("???");
            Team team = teamService.getTeam(teamId);
            List<Match> matches = matchService.getAllMatches(teamId.getEmail());
            List<Match> teamMatches = new ArrayList<>();
            for (Match match : matches) {
                if (match.getMatchId().getTeamHomeName().equals(teamId.getTeamName())
                        || match.getMatchId().getTeamAwayName().equals(teamId.getTeamName())) {
                    teamMatches.add(match);
                }
            }
            TeamDetails teamDetails = new TeamDetails(team, teamMatches);
            System.out.println("Team name: " + teamId.getTeamName());
            return new ResponseEntity<>(teamDetails, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/clearMatches")
    public ResponseEntity<List<Match>> clearMatches(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        matchService.clearData(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}