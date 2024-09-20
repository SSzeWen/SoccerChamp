package com.example.footballchampionship;

public class TeamScore {

    TeamScore(Team team) {
        teamName = team.getTeamId().getTeamName();
        groupNumber = team.getGroupNumber();
        registrationDate = team.getRegistrationDate();
        points = 0;
        goalsFor = 0;
        goalsAgainst = 0;
    }

    private String teamName;
    private int groupNumber;
    private java.time.LocalDate registrationDate;
    private int points;

    private int altPoints;
    private int goalsFor;
    private int goalsAgainst;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public java.time.LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(java.time.LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

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
