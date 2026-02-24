package project.adapter.in.web.BettingAccount.bettinAccountDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;
import project.domain.model.Enums.MatchKey;

public class CreateMatchEventPickDto {
    private Long matchId;
    @NotNull
    private MatchKey matchKey;
    @NotNull(message = "the outcomeName must not be nullCreateMatchEventPickDto 13")
    private String outcomeName;
    @Positive
    private double odd;
    @NotNull
    private League league;
    @NotNull
    private BetStatus status;

    public CreateMatchEventPickDto() {}


    public String getOutcomeName() { return outcomeName; }
    public void setOutcomeName(String outcomeName) { this.outcomeName = outcomeName; }

    public double getOdd() { return odd; }
    public void setOdd(double odd) { this.odd = odd; }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setLeague(League league) {
        this.league=league;
    }

    public League getLeague() {
        return league;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public BetStatus getStatus() {
        return status;
    }

    public MatchKey getMatchKey() {
        return this.matchKey;
    }

    public void setMatchKey(MatchKey matchKey) {
        this.matchKey = matchKey;
    }
}
