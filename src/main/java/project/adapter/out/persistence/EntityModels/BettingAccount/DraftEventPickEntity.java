package project.adapter.out.persistence.EntityModels.BettingAccount;

import jakarta.persistence.*;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;
import project.domain.model.Enums.MatchKey;

import java.time.Instant;


@Entity
public class DraftEventPickEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long identity;
    private MatchKey matchKey;
    private String OwnerMatchName;
    private String outcomeName;
    private double odd;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentBetSlipEntity_id", nullable = true)
    private DraftSlipEntity parentBetSlipEntity;
    @Enumerated
    private League league;
    private Instant begins;
    private BetStatus draftOutcomeStatus;

    public DraftEventPickEntity() {
    }


    public DraftSlipEntity getParent() {
        return parentBetSlipEntity;
    }

    public void setParent(DraftSlipEntity parent) {
        this.parentBetSlipEntity = parent;
    }

    public MatchKey getMatchKey() {
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

    public void setMatchKey(MatchKey matchKey) {
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

    public void setOwnerMatchName(String ownerMatchName) {
        this.OwnerMatchName = ownerMatchName;
    }

    public String getOwnerMatchName() {
        return OwnerMatchName;
    }

    public Instant getBegins() {
        return this.begins;
    }

    public void setBegins(Instant begins) {
        this.begins = begins;
    }

    public BetStatus getDraftOutcomeStatus() {
        return draftOutcomeStatus;
    }

    public void setDraftOutcomeStatus(BetStatus draftOutcomeStatus) {
        this.draftOutcomeStatus = draftOutcomeStatus;
    }
}



