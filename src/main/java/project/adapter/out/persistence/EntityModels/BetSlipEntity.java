package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BetSlipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "parentBetSlipEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchEventPickEntity> picks = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private BetStatus status;
    private BetCategory category;
    private Instant createdAt;
    private double totalOdd;

    //this attribute shows the betting account in which the betslip was placed
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentAccountEntity_id", nullable = false)
    private BettingAccountEntity parentAccountEntity;

    //this attribute shows the reducer in which the betslip was created
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "betSlipReducerParent_id", nullable = false)
    private ReducerEntity betSlipReducerParent;


    private BigDecimal stake;
    private BigDecimal potentialWinning;


    public BetSlipEntity() {
    }

    public void setTotalOdd(double totalOdd) {
        this.totalOdd = totalOdd;
    }

    public void setPotentialWinning(BigDecimal potentialWinning) {
        this.potentialWinning = potentialWinning;
    }

    public BigDecimal getPotentialWinning() {
        return potentialWinning;
    }

    public double getTotalOdd() {
        return totalOdd;
    }

    public void addMatchEventPickEntity(MatchEventPickEntity entity) {
        this.picks.add(entity);
        entity.setParent(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BetStatus getStatus() {
        return status;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public List<MatchEventPickEntity> getPicks() {
        return picks;
    }

    public BetCategory getCategory() {
        return category;
    }

    public void setPicks(List<MatchEventPickEntity> picks) {
        this.picks = picks;
    }


    public BettingAccountEntity getParentAccount() {
        return parentAccountEntity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getStake() {
        return stake;
    }


    public void setCategory(BetCategory category) {
        this.category = category;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setParentAccount(BettingAccountEntity parentAccount) {
        this.parentAccountEntity = parentAccount;
    }

    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }

    public void setBetSlipReducerParent(ReducerEntity betSlipReducerParent) {
        this.betSlipReducerParent = betSlipReducerParent;
    }

    public BettingAccountEntity getParentAccountEntity() {
        return parentAccountEntity;
    }
}
