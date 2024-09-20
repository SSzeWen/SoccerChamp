package com.example.footballchampionship;

public class TeamScore extends Team {

    TeamScore(Team team) {
        super.name = team.getName();
        super.groupNumber = team.getGroupNumber();
        super.registrationDate = team.getRegistrationDate();
        points = 0;
        goalsFor = 0;
        goalsAgainst = 0;
    }
    private int points;

    private int altPoints;
    private int goalsFor;
    private int goalsAgainst;

    public int getAltPoints() {
        return altPoints;
    }

    public void setAltPoints(int altPoints) {
        this.altPoints = altPoints;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
