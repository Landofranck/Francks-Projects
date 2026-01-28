package project.domain.model.Reducer;


import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Event;
import project.domain.model.MatchEventPick;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReducerBetSlip implements Event {

    private List<MatchEventPick> picks;
    private BetCategory category;
    private BrokerType brokerType;
    private Money planedStake;
    private Money remainingStake;
    private double totalOdds;
    private int numberOfEvents;
    private Money potentialWinning;

    public ReducerBetSlip(BetCategory category) {
        this.category = category;
        this.picks = new ArrayList<>();
        this.planedStake = new Money(BigDecimal.ZERO);
        this.remainingStake = new Money(BigDecimal.ZERO);
        this.totalOdds = 0;
        this.numberOfEvents = 0;
        this.potentialWinning = new Money(BigDecimal.ZERO);
    }

    public void makeTotalOdds() {
        double output = 1;
        for (MatchEventPick m : picks) {
            output *= m.getOdd();
        }
        this.totalOdds = output;
    }

    public void addMatchEventPick(MatchEventPick pick) {
        this.picks.add(pick);
        pick.setOwner(this);
        makeTotalOdds();
        this.numberOfEvents = picks.size();
    }

    public void removeMatchEventPicksByIndex(int i) {
        this.picks.remove(i);
        makeTotalOdds();
        this.numberOfEvents = picks.size();
        calculatePotentialWinning();
    }

    public void placeParBet(Money stake) {
        if (stake.isGreaterThan(this.remainingStake))
            throw new IllegalArgumentException("the bet ammount is greater than what is left to be bet");

       setRemainingStake( this.remainingStake.subtract(stake));

    }

    public void calculatePotentialWinning() {
        this.potentialWinning = new Money(planedStake.getValue().multiply(BigDecimal.valueOf(totalOdds)));
    }

    public List<MatchEventPick> getPicks() {
        return picks;
    }

    public void setPicks(List<MatchEventPick> picks) {
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

    public Money getPlanedStake() {
        return planedStake;
    }

    public void setPlanedStake(Money planedStake) {
        this.planedStake = planedStake;
    }

    public Money getRemainingStake() {
        return remainingStake;
    }

    public void setRemainingStake(Money remainingStake) {
        this.remainingStake = remainingStake;
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

    public Money getPotentialWinning() {
        return potentialWinning;
    }

    public void setPotentialWinning(Money potentialWinning) {
        this.potentialWinning = potentialWinning;
    }
}
