package project.adapter.out.persistence.EntityModels.BettingAccount;

import jakarta.persistence.*;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;
import project.domain.model.Enums.MatchKey;

import java.time.Instant;

@Entity
public class SlipEventPickEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long identity;
    private MatchKey matchKey;
    private String ownerMatchName;
    private String outcomeName;
    private double odd;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentBetSlipEntity_id",nullable = true)
    private BetSlipEntity parentBetSlipEntity;
    @Enumerated
    private League league;
    private BetStatus outComePickStatus;
    private Instant begins;

    public SlipEventPickEntity(){}




    public BetSlipEntity getParent() {
        return parentBetSlipEntity;
    }

    public void setParent(BetSlipEntity parent) {
        this.parentBetSlipEntity = parent;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BetSlipEntity getParentBetSlipEntity() {
        return parentBetSlipEntity;
    }

    public void setParentBetSlipEntity(BetSlipEntity parentBetSlipEntity) {
        this.parentBetSlipEntity = parentBetSlipEntity;
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

    public BetStatus getOutComePickStatus() {
        return this.outComePickStatus;
    }

    public void setOutComePickStatus(BetStatus outComePickStatus) {
        this.outComePickStatus = outComePickStatus;
    }

    public void setOwnerMatchName(String ownerMatchName) {
        this.ownerMatchName = ownerMatchName;
    }

    public String getOwnerMatchName() {
        return ownerMatchName;
    }

    public void setBegins(Instant begins) {
        this.begins=begins;
    }

    public Instant getBegins() {
        return begins;
    }
}
