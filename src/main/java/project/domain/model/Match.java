package project.domain.model;

import java.util.List;

public class Match {
    private Long matchId;
    private String home;
    private String away;
    private List<MatchEventPick> matchOutComes;

    public Match(long matchId, List<MatchEventPick> matchOutComes, String home, String away){
        this.matchId=matchId;
        this.matchOutComes=matchOutComes;
        this.home=home;
        this.away=away;
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
