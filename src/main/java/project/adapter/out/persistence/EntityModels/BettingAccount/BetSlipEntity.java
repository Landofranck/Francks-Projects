package project.adapter.out.persistence.EntityModels.BettingAccount;

import jakarta.persistence.*;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BetStrategy;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BetSlipEntity {
    private BetStrategy strategy;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "parentBetSlipEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SlipEventPickEntity> picks = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private BetStatus status;
    @Enumerated
    private BetCategory category;
    private Instant createdAt;
    private double totalOdd;

    //this attribute shows the betting account in which the betslip was placed
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentAccountEntity_id", nullable = true)
    private BettingAccountEntity parentAccountEntity;

    private BigDecimal stake;
    private BigDecimal potentialWinning;
    private Boolean bonusSlip;
    private double bonusOdds;

    public BetSlipEntity() {
    }


    public void addMatchEventPickEntity(SlipEventPickEntity entity) {
        this.picks.add(entity);
        entity.setParent(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SlipEventPickEntity> getPicks() {
        return picks;
    }

    public void setPicks(List<SlipEventPickEntity> picks) {
        this.picks = picks;
    }

    public BetStatus getStatus() {
        return status;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public BetCategory getCategory() {
        return category;
    }

    public void setCategory(BetCategory category) {
        this.category = category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalOdd() {
        return totalOdd;
    }

    public void setTotalOdd(double totalOdd) {
        this.totalOdd = totalOdd;
    }

    public BettingAccountEntity getParentAccountEntity() {
        return parentAccountEntity;
    }

    public void setParentAccountEntity(BettingAccountEntity parentAccountEntity) {
        this.parentAccountEntity = parentAccountEntity;
    }


    public BigDecimal getStake() {
        return stake;
    }

    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }

    public BigDecimal getPotentialWinning() {
        return potentialWinning;
    }

    public void setPotentialWinning(BigDecimal potentialWinning) {
        this.potentialWinning = potentialWinning;
    }

    public BetStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(BetStrategy strategy) {
        this.strategy = strategy;
    }

    public void setBonusSlip(Boolean bonusSlip){
        this.bonusSlip=bonusSlip;
    }
    public Boolean getBonusSlip() {
        return this.bonusSlip;
    }

    public double getBonusOdds() {
        return this.bonusOdds;
    }

    public void setBonusOdds(double bonusOdds) {
        this.bonusOdds = bonusOdds;
    }

}
