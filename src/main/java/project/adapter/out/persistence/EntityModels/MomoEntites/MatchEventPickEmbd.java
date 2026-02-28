package project.adapter.out.persistence.EntityModels.MomoEntites;

import jakarta.persistence.*;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;
import project.domain.model.Enums.MatchKey;

import java.time.Instant;


@Embeddable
public class MatchEventPickEmbd {
    private Long identity;
    private String ownerMatchName;
    private MatchKey matchKey;
    private String outcomeName;
    private double odd;
    private League league;
    private Instant begins;
    private BetStatus outComePickStatus;

    public MatchEventPickEmbd(Long identity, MatchKey matchKey, String outcomeName, double odd, League league){
        this.matchKey=matchKey;
        this.outcomeName=outcomeName;
        this.odd=odd;
        this.identity=identity;
        this.league=league;
    }

    public MatchEventPickEmbd() {
    }

    public MatchKey getMatchKey() {
        return matchKey;
    }

    public void setMatchKey(MatchKey matchKey) {
        this.matchKey = matchKey;
    }

    public String getOutcomeName() {
        return outcomeName;
    }

    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }

    public double getOdd() {
        return odd;
    }

    public void setOdd(double odd) {
        this.odd = odd;
    }

    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    public Long getIdentity() {
        return identity;
    }

    public League getLeague() {
        return this.league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public void setOwnerMatchName(String ownerMatchName) {
        this.ownerMatchName = ownerMatchName;
    }

    public String getOwnerMatchName() {
        return ownerMatchName;
    }

    public void setBegins(Instant begins) {
        this.begins = begins;
    }

    public Instant getBegins() {
        return begins;
    }

    public BetStatus getOutcomePickStatus() {
        return outComePickStatus;
    }

    public void setOutComePickStatus(BetStatus outComePickStatus) {
        this.outComePickStatus = outComePickStatus;
    }

}
