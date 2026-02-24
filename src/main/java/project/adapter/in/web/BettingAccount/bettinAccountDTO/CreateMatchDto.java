package project.adapter.in.web.BettingAccount.bettinAccountDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Enums.League;

import java.time.Instant;
import java.util.List;

public class CreateMatchDto {
    @NotNull
    private String home;
    @NotNull
    private String away;
    @NotNull()
    private BrokerType broker;
    @NotNull
    private League matchLeague;
    @NotNull(message = "you cannot create match without outcomes: MatchDto 22")
    @NotEmpty(message = "you cannot create match without outcomes: MatchDto 23")
    private List<@Valid CreateMatchEventPickDto> matchOutComes;
    @NotNull(message = "Match must have a begin date and time ")
    private Instant begins;
    private Instant ends;
    private boolean bonusMatch;

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }


    public List<CreateMatchEventPickDto> getMatchOutComes() {
        return matchOutComes;
    }

    public void setBroker(BrokerType broker) {
        this.broker = broker;
    }

    public League getMatchLeague() {
        return matchLeague;
    }

    public BrokerType getBroker() {
        return broker;
    }

    public Instant getBegins() {
        return this.begins;
    }

    public boolean isBonusMatch() {
        return this.bonusMatch;
    }

    public Instant getEnds() {
        return ends;
    }
}
