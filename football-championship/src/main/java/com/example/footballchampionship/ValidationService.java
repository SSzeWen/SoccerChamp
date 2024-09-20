package com.example.footballchampionship;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidationService {

    public void validateMatches(List<Team> teams, List<Match> matches, List<Match> currentMatches) {
        List<String> teamNames = new ArrayList<>();
        List<String> matchNames = new ArrayList<>();
        for (Team team : teams) {
            teamNames.add(team.getName());
        }

        for (Match match : matches) {
            if (!teamNames.contains(match.getId().getTeamHomeName()) || !teamNames.contains(match.getId().getTeamAwayName())) {
                throw new IllegalArgumentException("Invalid team name in match: " + match.getId().getTeamHomeName() + " vs "
                        + match.getId().getTeamAwayName());
            }
        }

        for (Match match : matches) {
            for (Match currentMatch : currentMatches) {
                if (match.getId().getTeamHomeName().equals(currentMatch.getId().getTeamHomeName()) &&
                        match.getId().getTeamAwayName().equals(currentMatch.getId().getTeamAwayName())) {
                    throw new IllegalArgumentException("Duplicate match in current matches: " + match.getId().getTeamHomeName() + " vs "
                            + match.getId().getTeamAwayName());
                }
            }
        }
    }

    public List<TeamScore> calculateRankings(List<Match> matches, List<Team> teams) {
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
}