package project.domain.model.Reducer;


import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Event;
import project.domain.model.MatchOutComePick;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ReducerBetSlip implements Event {

    private List<MatchOutComePick> picks;
    private BetCategory category;
    private BrokerType brokerType;
    private Money planedStake;
    private Money remainingStake;
    private double totalOdds;
    private int numberOfEvents;
    private Money potentialWinning;
    private BetStrategy betStrategy;

    public ReducerBetSlip(BetCategory category, BetStrategy strategy) {
        this.category = category;
        this.picks = new ArrayList<>();
        this.planedStake = new Money(BigDecimal.ZERO);
        this.remainingStake = new Money(BigDecimal.ZERO);
        this.totalOdds = 0;
        this.numberOfEvents = 0;
        this.potentialWinning = new Money(BigDecimal.ZERO);
        this.betStrategy = strategy;
    }

    public void makeTotalOdds() {
        double output = 1;
        for (MatchOutComePick m : picks) {
            output *= m.getOdd();
        }
        this.totalOdds = output;
    }

    public void addMatchEventPick(MatchOutComePick pick) {
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

    public void placeParBet(Money stake, Boolean t) {


        if (t) {
            if (stake.isGreaterOrEqual(this.remainingStake))
                setRemainingStake(new Money(0));
            else
                setRemainingStake(this.remainingStake.subtract(stake));

        } else if (stake.isGreaterThan(this.remainingStake)) {
            throw new IllegalArgumentException("the bet ammount is greater than what is left to be bet");
        } else {
            setRemainingStake(this.remainingStake.subtract(stake));
        }

    }

    public void updateRemainingStake(BigDecimal oldRemainingStake, double oldOdd) {
        if (this.totalOdds <= 0.0) throw new IllegalStateException("totalOdds must be > 0");

        BigDecimal numerator = BigDecimal.valueOf(oldOdd);
        BigDecimal denominator = BigDecimal.valueOf(this.totalOdds);

        BigDecimal ratio = numerator.divide(denominator, 10, RoundingMode.HALF_EVEN); // pick scale you want
        this.remainingStake = new Money(oldRemainingStake.multiply(ratio));
    }

    public void calculatePotentialWinning() {
        this.potentialWinning = new Money(planedStake.getValue().multiply(BigDecimal.valueOf(totalOdds)));
    }

    public void upDatePotentialWinning(Money potentialWinning) {
        this.potentialWinning = new Money(planedStake.getValue().multiply(BigDecimal.valueOf(totalOdds)));
    }

    public List<MatchOutComePick> getPicks() {
        return picks;
    }

    public void setPicks(List<MatchOutComePick> picks) {
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

    public void setBetStrategy(BetStrategy betStrategy) {
        this.betStrategy = betStrategy;
    }

    public BetStrategy getBetStrategy() {
        return betStrategy;
    }
}
