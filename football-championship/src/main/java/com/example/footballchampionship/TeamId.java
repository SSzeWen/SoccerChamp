package com.example.footballchampionship;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class TeamId implements Serializable {
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    // JPA requires a no-arg constructor
    public TeamId() {}
    public TeamId(String email, String teamName) {
        this.email = email;
        this.teamName = teamName;
    }

    // Getters and setters for email and teamName

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamId teamId = (TeamId) o;
        return email.equals(teamId.email) && teamName.equals(teamId.teamName);
    }
}