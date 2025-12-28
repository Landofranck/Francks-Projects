package project.adapter.in.web;


import jakarta.validation.constraints.NotNull;

import java.util.List;

public class MatchDto {
    private Long id;
    @NotNull
    private String home;
    @NotNull
    private String away;
    @NotNull
    private List<MatchEventPickDto> matchOutComes;

    public MatchDto() {
    }


    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public List<MatchEventPickDto> getMatchOutComes() {
        if (matchOutComes == null || matchOutComes.isEmpty()) throw new IllegalArgumentException("no matches to get MatchDto 38");
        return matchOutComes;
    }

    public void setMatchOutComes(List<MatchEventPickDto> matchOutComes) {
        this.matchOutComes = matchOutComes;
    }

    public void setId(long matchId) {
        this.id = matchId;
    }

    public Long getId() {
        return id;
    }
}


