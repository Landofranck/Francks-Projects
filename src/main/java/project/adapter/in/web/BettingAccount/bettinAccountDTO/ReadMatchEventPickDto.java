package project.adapter.in.web.BettingAccount.bettinAccountDTO;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import project.adapter.in.web.Utils.Link;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;
import project.domain.model.Enums.MatchKey;

import java.util.ArrayList;
import java.util.List;

public class ReadMatchEventPickDto {
    private Long matchId;
    private MatchKey matchKey;
    @NotNull
    private String ownerMatchName;
    @NotNull
    private String outcomeName;
    @Positive
    private double odd;
    @NotNull
    private League league;
    @NotNull
    private BetStatus status;
    private List<Link> links=new ArrayList<>();

    public ReadMatchEventPickDto() {}

    public MatchKey getMatchKey() { return matchKey; }
    public void setMatchKey(MatchKey matchKey) { this.matchKey = matchKey; }

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

    public void setLinks(List<Link> update) {
        this.links = update;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setOwnerMatchName(String ownerMatchName) {
        this.ownerMatchName = ownerMatchName;
    }

    public String getOwnerMatchName() {
        return ownerMatchName;
    }
}
