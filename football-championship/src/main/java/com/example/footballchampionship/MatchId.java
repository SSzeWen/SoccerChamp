package com.example.footballchampionship;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class MatchId implements Serializable {

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "team_home_name", nullable = false)
    private String teamHomeName;

    @Column(name = "team_away_name", nullable = false)
    private String teamAwayName;

    public MatchId() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchId matchId = (MatchId) o;
        return email.equals(matchId.email) &&
                teamHomeName.equals(matchId.teamHomeName) &&
                teamAwayName.equals(matchId.teamAwayName);
    }
}