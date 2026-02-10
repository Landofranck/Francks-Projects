package project.adapter.out.persistence.EntityModels.BettingAccount;

import jakarta.persistence.*;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;


@Entity
public class DraftEventPickEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long identity;
    private String matchKey;
    private String outcomeName;
    private double odd;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentBetSlipEntity_id", nullable = true)
    private DraftSlipEntity parentBetSlipEntity;
    @Enumerated
    private League league;

    public DraftEventPickEntity() {
    }


    public DraftSlipEntity getParent() {
        return parentBetSlipEntity;
    }

    public void setParent(DraftSlipEntity parent) {
        this.parentBetSlipEntity = parent;
    }

    public String matchKey() {
        return matchKey;
    }

    public String getMatchKey() {
        return matchKey;
    }

    public String outcomeName() {
        return outcomeName;
    }

    public String getOutcomeName() {
        return outcomeName;
    }

    public double odd() {
        return odd;
    }

    public double getOdd() {
        return odd;
    }

    public Long getId() {
        return id;
    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public void setOdd(double odd) {
        this.odd = odd;
    }

    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
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



