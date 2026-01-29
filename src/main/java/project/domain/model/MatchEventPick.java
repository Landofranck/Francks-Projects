package project.domain.model;


import project.domain.model.Enums.League;

import java.util.Objects;

public class MatchEventPick {
    private Long identity;
    private String matchKey;
    private final String outcomeName;
    private final double odd;
    private Event owner;
    private League league;

    //when a pick is created we should only know the outcome  and odds first, match key and owner is added later
    public MatchEventPick(Long id,String matchKey, String outcomeName, double odd, League league) {
        if(odd <=0)throw new IllegalArgumentException("odd must be > 0");
        this.outcomeName = outcomeName;
        this.odd = Objects.requireNonNull(odd, "odd");
        this.matchKey=matchKey;
        this.identity =id;
        this.league=league;
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

    public double getOdd() {
        return odd;
    }

    public Event getOwner() {
        return owner;
    }

    public void setOwner(Event owner) {
        this.owner = owner;
    }

    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    public Long getIdentity() {
        return identity;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public League getLeague() {
        return league;
    }
}
