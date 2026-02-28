package project.domain.model;


import project.adapter.in.web.Utils.Code;
import project.application.error.ValidationException;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;
import project.domain.model.Enums.MatchKey;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public class MatchOutComePick {
    private Long id;
    private Long identity;
    private MatchKey matchKey;
    private String ownerMatchName;
    private Instant begins;
    private final String outcomeName;
    private final double odd;
    private Event owner;
    private League league;
    private BetStatus outcomePickStatus;

    //when a pick is created we should only know the outcome  and odds first, match key and owner is added later
    public MatchOutComePick(Long matchId, MatchKey matchKey, String outcomeName, double odd, League league) {
        if(odd <=0)throw new ValidationException(Code.INVALID_AMOUNT,"odd must be > 0 MatchOutcomePick 27", Map.of("matchIds",matchId));
        this.outcomeName = outcomeName;
        this.odd = odd;
        this.matchKey=matchKey;
        this.identity = matchId;
        this.league=league;
        this.outcomePickStatus=BetStatus.CREATING;
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

    public BetStatus getOutcomePickStatus() {
        return outcomePickStatus;
    }

    public void setOutcomePickStatus(BetStatus outcomePickStatus) {
        this.outcomePickStatus = outcomePickStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerMatchName() {
        return ownerMatchName;
    }

    public void setOwnerMatchName(String ownerMatchName) {
        this.ownerMatchName = ownerMatchName;
    }

    public void setBegins(Instant begins) {
        this.begins = begins;
    }

    public Instant getBegins() {
        return begins;
    }
}
