package com.example.footballchampionship;

import java.util.List;

public class TeamDetails {
    private Team team;
    private List<Match> matches;

    public TeamDetails(Team team, List<Match> matches) {
        this.team = team;
        this.matches = matches;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
