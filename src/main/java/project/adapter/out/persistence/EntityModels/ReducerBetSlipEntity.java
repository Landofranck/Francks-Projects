package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.adapter.out.persistence.EntityModels.MomoEntites.MatchEventPickEmbd;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ReducerBetSlipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ElementCollection
    private List<MatchEventPickEmbd> picks=new ArrayList<>();
    private BetCategory category;
    private BrokerType brokerType;
    private BigDecimal planedStake;
    private BigDecimal RemainingStake;
    private double totalOdds;
    private int numberOfEvents;
    private BigDecimal potentialWinning;
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "reducer_parent_id",nullable = false)
    private ReducerEntity reducerParent;
    @Enumerated
    private BetStrategy betStrategy;

    public void addMatchEventPickEntity(MatchEventPickEmbd entity) {
        this.picks.add(entity);
    }

    public ReducerEntity getReducerParent() {
        return reducerParent;
    }

    public void setReducerParent(ReducerEntity reducerParent) {
        this.reducerParent = reducerParent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MatchEventPickEmbd> getPicks() {
        return picks;
    }

    public void setPicks(List<MatchEventPickEmbd> picks) {
        this.picks = picks;
    }

    public BetCategory getCategory() {
        return category;
    }

    public void setCategory(BetCategory category) {
        this.category = category;
    }

    public BrokerType getBrokerType() {
        return brokerType;
    }

    public void setBrokerType(BrokerType brokerType) {
        this.brokerType = brokerType;
    }

    public BigDecimal getPlanedStake() {
        return planedStake;
    }

    public void setPlanedStake(BigDecimal planedStake) {
        this.planedStake = planedStake;
    }

    public BigDecimal getRemainingStake() {
        return RemainingStake;
    }

    public void setRemainingStake(BigDecimal remainingStake) {
        RemainingStake = remainingStake;
    }

    public double getTotalOdds() {
        return totalOdds;
    }

    public void setTotalOdds(double totalOdds) {
        this.totalOdds = totalOdds;
    }

    public int getNumberOfEvents() {
        return numberOfEvents;
    }

    public void setNumberOfEvents(int numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
    }

    public BigDecimal getPotentialWinning() {
        return potentialWinning;
    }

    public void setPotentialWinning(BigDecimal potentialWinning) {
        this.potentialWinning = potentialWinning;
    }

    public BetStrategy getBetStrategy() {
        return this.betStrategy;
    }

    public void setBetStrategy(BetStrategy betStrategy) {
        this.betStrategy = betStrategy;
    }
}
