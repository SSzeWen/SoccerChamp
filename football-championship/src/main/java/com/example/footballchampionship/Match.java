package com.example.footballchampionship;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "matches",
        uniqueConstraints = @UniqueConstraint(columnNames = {"team_a_name", "team_b_name"}))
public class Match {
    @EmbeddedId
    private MatchId id;

    @Column(name = "team_home_goals", nullable = false)
    private int teamHomeGoals;

    @Column(name = "team_away_goals", nullable = false)
    private int teamAwayGoals;

    public MatchId getId() {
        return id;
    }
    public int getTeamHomeGoals() {
        return teamHomeGoals;
    }

    public void setTeamHomeGoals(int teamHomeGoals) {
        this.teamHomeGoals = teamHomeGoals;
    }

    public int getTeamAwayGoals() {
        return teamAwayGoals;
    }

    public void setTeamAwayGoals(int teamAwayGoals) {
        this.teamAwayGoals = teamAwayGoals;
    }
}
