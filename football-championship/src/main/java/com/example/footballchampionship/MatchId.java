package com.example.footballchampionship;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class MatchId implements Serializable {
    @Column(name = "team_home_name", nullable = false)
    private String teamHomeName;

    @Column(name = "team_away_name", nullable = false)
    private String teamAwayName;

    public String getTeamHomeName() {
        return teamHomeName;
    }

    public void setTeamHomeName(String teamHomeName) {
        this.teamHomeName = teamHomeName;
    }

    public String getTeamAwayName() {
        return teamAwayName;
    }

    public void setTeamAwayName(String teamAwayName) {
        this.teamAwayName = teamAwayName;
    }
}