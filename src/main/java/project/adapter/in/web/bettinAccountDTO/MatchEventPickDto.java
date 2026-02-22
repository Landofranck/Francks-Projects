package project.adapter.in.web.bettinAccountDTO;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import project.adapter.in.web.Utils.Link;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;

public class MatchEventPickDto {
    private Long matchId;
    @NotNull
    private String matchKey;
    @NotNull
    private String outcomeName;
    @Positive
    private double odd;
    @NotNull
    private League league;
    @NotNull
    private BetStatus status;
    private Link update;

    public MatchEventPickDto() {}

    public String getMatchKey() { return matchKey; }
    public void setMatchKey(String matchKey) { this.matchKey = matchKey; }

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

    public void setUpdate(Link update) {
        this.update = update;
    }

    public Link getUpdate() {
        return update;
    }
}
