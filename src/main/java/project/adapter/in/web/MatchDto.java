package project.adapter.in.web;

import project.domain.model.MatchEventPick;

import java.util.List;
import java.util.Objects;

public class MatchDto {
    private Long matchId;
    private String home;
    private String away;
    private List<MatchEventPickDto> matchOutComes;

    public Match(long matchId, List<MatchEventPick> matchOutComes, String home, String away) {
        this.matchId = matchId;
        this.matchOutComes = matchOutComes;
        this.home = home;
        this.away = away;
    }

    public void createPick(double odds, String outcome) {
        MatchEventPick pick = new MatchEventPick(outcome, odds);
        pick.setMatchKey(getHome()+" vs "+getAway());
        addPick(pick);
    }

    public void addPick(MatchEventPick pick) {
        Objects.requireNonNull(pick, "error while adding pick to match");
        this.matchOutComes.add(pick);

    }

    public long getMatchId() {
        return matchId;
    }

    public List<MatchEventPick> getMatchOutComes() {
        return matchOutComes;
    }

    public void setMatchOutComes(List<MatchEventPick> matchOutComes) {
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

