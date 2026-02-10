package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEntity;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;

@Entity
public class MatchOutcomeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long identity;
    private String matchKey;
    private String outcomeName;
    private Double odd;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentMatch_id", nullable = false)
    private MatchEntity parentMatch;
    @Enumerated
    private League league;
    private BetStatus outComePickStatus;

    public MatchOutcomeEntity() {

    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public String getMatchKey() {
        return matchKey;
    }

    public MatchEntity getParentMatch() {
        return parentMatch;
    }

    public void setParentMatch(MatchEntity parentMatch) {
        this.parentMatch = parentMatch;
    }

    public Long getId() {
        return id;
    }

    public void setOdd(Double odd) {
        this.odd = odd;
    }

    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }

    public String getOutcomeName() {
        return outcomeName;
    }

    public Double getOdd() {
        return odd;
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

    public void setOutcomePickStatus(BetStatus outcomePickStatus) {
        this.outComePickStatus=outcomePickStatus;
    }

    public BetStatus getOutComePickStatus() {
        return outComePickStatus;
    }
}
