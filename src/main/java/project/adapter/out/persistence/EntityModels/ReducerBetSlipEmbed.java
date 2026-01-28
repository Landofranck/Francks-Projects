package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEventPickEntity;
import project.adapter.out.persistence.EntityModels.MomoEntites.MatchEventPickEmbd;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BrokerType;
import project.domain.model.MatchEventPick;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.util.List;

@Embeddable
public class ReducerBetSlipEmbed {
    @ElementCollection
    private List<MatchEventPickEmbd> picks;
    private BetCategory category;
    private BrokerType brokerType;
    private BigDecimal planedStake;
    private BigDecimal RemainingStake;
    private double totalOdds;
    private int numberOfEvents;
    private BigDecimal potentialWinning;
    public void addMatchEventPickEntity(MatchEventPickEmbd entity) {
        this.picks.add(entity);
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
}
