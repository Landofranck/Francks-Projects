package project.adapter.in.web;


import jakarta.validation.constraints.NotNull;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Enums.League;

import java.util.List;

public class MatchDto {
    private Long id;
    @NotNull
    private String home;
    @NotNull
    private String away;
    @NotNull
    private List<MatchEventPickDto> matchOutComes;
    @NotNull()
    private BrokerType broker;
    @NotNull
    private League matchLeague;


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

    public void setBroker(BrokerType broker) {
        this.broker = broker;
    }

    public void setMatchLeague(League matchLeague) {
        this.matchLeague =matchLeague;
    }

    public League getMatchLeague() {
        return matchLeague;
    }

    public BrokerType getBroker() {
        return broker;
    }
}


