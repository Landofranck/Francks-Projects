package project.domain.model;

import project.domain.model.Enums.BrokerType;
import project.domain.model.Enums.League;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Match implements Event {
    private Long matchId;
    private League matchLeague;
    private String home;
    private String away;
    private BrokerType broker;
    private List<MatchOutComePick> matchOutComes=new ArrayList<>();
    private Long version;

    public Match(String home, String away) {
        this.home = home;
        this.away = away;
    }



    public void addPick(MatchOutComePick pick) {
        Objects.requireNonNull(pick, "error while adding pick to match");
        this.matchOutComes.add(pick);
        pick.setMatchKey(home+" vs "+ away);
        pick.setOwner(this);
        pick.setIdentity(matchId);
        pick.setLeague(this.matchLeague);
    }

    public long getMatchId() {
        return matchId;
    }

    public List<MatchOutComePick> getMatchOutComes() {
        return matchOutComes;
    }

    public void setMatchOutComes(List<MatchOutComePick> matchOutComes) {
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

    public void setBroker(BrokerType broker) {
        this.broker = broker;
    }

    public BrokerType getBroker() {
        return broker;
    }

    public void setMatchLeague(League matchLeague) {
        this.matchLeague = matchLeague;
    }

    public League getMatchLeague() {
        return matchLeague;
    }

    public void setVersion(Long version) {
        this.version=version;
    }

    public Long getVersion() {
        return version;
    }
}
