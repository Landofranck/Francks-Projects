package project.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Match implements Event {
    private Long matchId;
    private String home;
    private String away;
    private List<MatchEventPick> matchOutComes=new ArrayList<>();

    public Match(String home, String away) {
        this.home = home;
        this.away = away;
    }



    public void addPick(MatchEventPick pick) {
        Objects.requireNonNull(pick, "error while adding pick to match");
        this.matchOutComes.add(pick);
        pick.setMatchKey(home+" vs "+ away);
        pick.setOwner(this);

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
