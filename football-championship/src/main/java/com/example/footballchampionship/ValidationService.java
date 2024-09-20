package com.example.footballchampionship;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidationService {

    public void validateMatches(List<Team> teams, List<Match> matches, List<Match> currentMatches) {
        List<TeamId> teamIds = new ArrayList<>();
        List<TeamId> matchTeamIds = new ArrayList<>();
        for (Team team : teams) {
            teamIds.add(team.getTeamId());
        }

        for (Match match : matches) {
            TeamId homeTeamId = new TeamId(match.getMatchId().getEmail(), match.getMatchId().getTeamHomeName());
            TeamId awayTeamId = new TeamId(match.getMatchId().getEmail(), match.getMatchId().getTeamAwayName());
            boolean homeTeamExists = false;
            boolean awayTeamExists = false;
            for (TeamId teamId : teamIds) {
                if (teamId.equals(homeTeamId)) {
                    homeTeamExists = true;
                    break;
                }
            }
            for (TeamId teamId : teamIds) {
                if (teamId.equals(awayTeamId)) {
                    awayTeamExists = true;
                    break;
                }
            }

            if (!homeTeamExists || !awayTeamExists) {
                throw new IllegalArgumentException("Invalid team name in match: " + match.getMatchId().getTeamHomeName()
                        + " vs " + match.getMatchId().getTeamAwayName());
            }
        }

        for (Match match : matches) {
            for (Match currentMatch : currentMatches) {
                if (match.getMatchId().equals(currentMatch.getMatchId())) {
                    throw new IllegalArgumentException("Duplicate match in current matches: "
                            + match.getMatchId().getTeamHomeName() + " vs " + match.getMatchId().getTeamAwayName());
                }
            }
        }
    }

    public List<TeamScore> calculateRankings(List<Match> matches, List<Team> teams) {
        Map<String, TeamScore> teamStats = new HashMap<>();

        for (Team team : teams) {
            teamStats.put(team.getTeamId().getTeamName(), new TeamScore(team));
        }

        for (Match match : matches) {
            String teamAName = match.getMatchId().getTeamHomeName();
            String teamBName = match.getMatchId().getTeamAwayName();
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