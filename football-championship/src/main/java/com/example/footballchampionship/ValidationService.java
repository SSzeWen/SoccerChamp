package com.example.footballchampionship;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}