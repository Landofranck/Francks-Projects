package project.domain.model;

import project.domain.model.Enums.BrokerType;
import project.domain.model.Enums.League;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.sql.Time;
import java.time.Instant;
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
    private Instant begins;

    private Instant ends;
    private boolean isBonusMatch;

    public Match(String home, String away, BrokerType broker,Instant begins, Instant ends) {
        this.home = home;
        this.away = away;
        this.broker = broker;
        this.begins=begins;
        this.ends=ends;
    }

    public Instant getEnds() {
        return ends;
    }

    public void setEnds(Instant ends) {
        this.ends = ends;
    }

    public Instant getBegins() {
        return begins;
    }

    public void setBegins(Instant begins) {
        this.begins = begins;
    }

    public boolean isBonusMatch() {
        return isBonusMatch;
    }

    public void setBonusMatch(boolean bonusMatch) {
        isBonusMatch = bonusMatch;
    }



    public void addPick(MatchOutComePick pick) {
        Objects.requireNonNull(pick, "error while adding pick to match");
        pick.setOwnerMatchName(home+" vs "+ away);
        pick.setOwner(this);
        pick.setIdentity(matchId);
        pick.setLeague(this.matchLeague);
        this.matchOutComes.add(pick);
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
