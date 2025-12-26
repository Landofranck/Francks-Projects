package project.domain.model;

import java.util.List;

public class Match {
    private Long matchId;
    private List<MatchEventPick> matchOutComes;

    public Match(long matchId, List<MatchEventPick> matchOutComes){
        this.matchId=matchId;
        this.matchOutComes=matchOutComes;
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
}
