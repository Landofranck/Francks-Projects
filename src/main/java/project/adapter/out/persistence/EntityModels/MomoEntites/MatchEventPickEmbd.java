package project.adapter.out.persistence.EntityModels.MomoEntites;

import jakarta.persistence.*;
import project.domain.model.Enums.League;


@Embeddable
public class MatchEventPickEmbd {
    private Long identity;
    private String matchKey;
    private String outcomeName;
    private double odd;
    private League league;

    public MatchEventPickEmbd(Long identity,String matchKey,String outcomeName,double odd){
        this.matchKey=matchKey;
        this.outcomeName=outcomeName;
        this.odd=odd;
        this.identity=identity;
    }

    public MatchEventPickEmbd() {
    }

    public String getMatchKey() {
        return matchKey;
    }

    public void setMatchKey(String matchKey) {
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
}
