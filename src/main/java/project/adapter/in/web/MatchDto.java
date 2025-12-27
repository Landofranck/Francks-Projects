package project.adapter.in.web;

import project.domain.model.MatchEventPick;

import java.util.List;
import java.util.Objects;

public class MatchDto {
    private Long matchId;
    private String home;
    private String away;
    private List<MatchEventPickDto> matchOutComes;




    public long getMatchId() {
        return matchId;
    }

    public List<MatchEventPickDto> getMatchOutComes() {
        return matchOutComes;
    }

    public void setMatchOutComes(List<MatchEventPickDto> matchOutComes) {
        this.matchOutComes = matchOutComes;
    }

    public String getHome() {
        return home;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getAway() {
        return away;
    }
}

