package com.example.footballchampionship;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
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

    public List<TeamScore> calculateRankings() {
        List<Match> matches = matchService.getAllMatches();
        List<Team> teams = teamService.getAllTeams();
        Map<String, TeamScore> teamStats = new HashMap<>();

        for (Team team : teams) {
            teamStats.put(team.getName(), new TeamScore(team));
        }

        for (Match match : matches) {
            String teamAName = match.getId().getTeamHomeName();
            String teamBName = match.getId().getTeamAwayName();
            int teamAGoals = match.getTeamHomeGoals();
            int teamBGoals = match.getTeamAwayGoals();

            TeamScore teamA = teamStats.get(teamAName);
            TeamScore teamB = teamStats.get(teamBName);

            teamA.setGoalsFor(teamA.getGoalsFor() + teamAGoals);
            teamA.setGoalsAgainst(teamA.getGoalsAgainst() + teamBGoals);
            teamB.setGoalsFor(teamB.getGoalsFor() + teamBGoals);
            teamB.setGoalsAgainst(teamB.getGoalsAgainst() + teamAGoals);

            if (teamAGoals > teamBGoals) {
                teamA.setPoints(teamA.getPoints() + 3);
                teamA.setAltPoints(teamA.getAltPoints() + 5);
                teamB.setAltPoints(teamB.getAltPoints() + 1);
            } else if (teamAGoals < teamBGoals) {
                teamB.setPoints(teamB.getPoints() + 3);
                teamB.setAltPoints(teamB.getAltPoints() + 5);
                teamA.setAltPoints(teamA.getAltPoints() + 1);
            } else {
                teamA.setPoints(teamA.getPoints() + 1);
                teamB.setPoints(teamB.getPoints() + 1);
                teamA.setAltPoints(teamA.getAltPoints() + 3);
                teamB.setAltPoints(teamB.getAltPoints() + 3);
            }

            teamStats.put(teamAName, teamA);
            teamStats.put(teamBName, teamB);
        }

        List<TeamScore> sortedTeams = new ArrayList<>(teamStats.values());
        sortedTeams.sort((a, b) -> {
            if (b.getGroupNumber() != a.getGroupNumber()) {
                return b.getGroupNumber() - a.getGroupNumber();
            }
            if (b.getPoints() != a.getPoints()) {
                return b.getPoints() - a.getPoints();
            }
            if (b.getGoalsFor() != a.getGoalsFor()) {
                return b.getGoalsFor() - a.getGoalsFor();
            }
            if (b.getAltPoints() != a.getAltPoints()) {
                return b.getAltPoints() - a.getAltPoints();
            }
            // Final tiebreaker is the earliest registration date
            return a.getRegistrationDate().compareTo(b.getRegistrationDate());
        });

        return sortedTeams;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/rankings")
    public ResponseEntity<List<TeamScore>> getRankings() {
        List<TeamScore> teamScore = calculateRankings();
        if (teamScore != null && !teamScore.isEmpty()) {
            return new ResponseEntity<>(teamScore, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}