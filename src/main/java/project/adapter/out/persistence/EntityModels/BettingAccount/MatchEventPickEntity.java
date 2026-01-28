package project.adapter.out.persistence.EntityModels.BettingAccount;

import jakarta.persistence.*;
@Entity
public class MatchEventPickEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long identity;
    private String matchKey;
    private String outcomeName;
    private double odd;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentBetSlipEntity_id",nullable = true)
    private BetSlipEntity parentBetSlipEntity;

    public MatchEventPickEntity(){}




    public BetSlipEntity getParent() {
        return parentBetSlipEntity;
    }

    public void setParent(BetSlipEntity parent) {
        this.parentBetSlipEntity = parent;
    }

    public String matchKey() {
        return matchKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
