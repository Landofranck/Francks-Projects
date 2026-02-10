package project.domain.model;

import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BetSlip implements Event {
    private Long id;
    private List<MatchOutComePick> picks;
    private BetStrategy strategy;
    private BetStatus status;
    private BetCategory category;
    private Instant createdAt;
    private BrokerType brokerType;
    //int the reducer method betslips will be created without parent accounts
    private BettingAccount parentAccount;
    private Money stake;
    private double totalOdds;
    private double bonusOdds;
    private int numberOfEvents;
    private Money potentialWinning;
    private Boolean bonusSlip;

    public BetSlip(Boolean bonusSlip, BetStrategy strategy) {
        this.picks = new ArrayList<>();
        this.status = BetStatus.PENDING;
        this.stake = new Money(BigDecimal.ZERO);
        this.totalOdds = 0;
        this.bonusOdds = 0;
        this.strategy = strategy;
        this.bonusSlip = bonusSlip;
    }

    //this method calculates the odds for the slip and accummulator bonus odds
    public void makeTotalOdds() {
        double output = 1;
        int count = 1;
        double bonusOutput = 1;
        if (this.brokerType == BrokerType.ONE_X_BET) {

            for (MatchOutComePick m : picks) {
                output *= m.getOdd();
                if (count == 3)
                    bonusOutput = 1.03;
                if (count > 3)
                    bonusOutput += 0.01;
                count++;
            }

            this.totalOdds = output * bonusOutput;
        } else if (this.brokerType == BrokerType.BETPAWA) {
            for (MatchOutComePick m : picks) {
                output *= m.getOdd();
                if (count == 3)
                    bonusOutput = 1.03;
                if (count == 4)
                    bonusOutput = 1.05;
                if (count > 4)
                    bonusOutput += 0.05;
                if (count > 20)
                    bonusOutput += 1.1;
                if (count > 29)
                    bonusOutput += 1.15;
                if (count == 28)
                    bonusOutput = 2.85;
                if (m.getOdd() > 1.2)
                    count++;
            }

            this.totalOdds = (((100+ (output*100)-100)+((output*100)-100)*(bonusOutput-1))/100);
        } else {
            for (MatchOutComePick m : picks) {
                output *= m.getOdd();
            }
        }
        this.bonusOdds = bonusOutput;
    }

    public void addMatchEventPick(MatchOutComePick pick) {
        this.picks.add(pick);
        pick.setOwner(this);
        makeTotalOdds();
        this.numberOfEvents = picks.size();
        updateCategory();
    }

    public void updateCategory() {
        if (picks.size() > 1) {
            category = BetCategory.COMBINATION;
        } else {
            category = BetCategory.SINGLE;
        }
    }

    public void removeMatchEventPicksByIndex(int i) {
        this.picks.remove(i);
        makeTotalOdds();
        this.numberOfEvents = picks.size();
        calculatPotentialWinning();
        updateCategory();
    }

    public void calculatPotentialWinning() {
        this.potentialWinning = new Money(stake.getValue().multiply(BigDecimal.valueOf(totalOdds)));
    }

    public void setPotentialWinning(Money potentialWinning) {
        this.potentialWinning = potentialWinning;
    }

    public Money getPotentialWinning() {
        return potentialWinning;
    }

    public int getNumberOfEvents() {
        return numberOfEvents;
    }

    public double getTotalOdds() {
        return totalOdds;
    }

    public void setParentAccount(BettingAccount parentAccount) {
        this.parentAccount = parentAccount;
    }

    public Long getId() {
        return id;
    }


    public BetStatus getStatus() {
        return status;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public List<MatchOutComePick> getPicks() {
        return picks;
    }

    public BetCategory getCategory() {
        return category;
    }

    public void setCategory(BetCategory category) {
        this.category = category;
    }

    public void setPicks(List<MatchOutComePick> picks) {
        this.picks = picks;
    }

    public void setStake(Money stake) {
        this.stake = stake;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public BettingAccount getParentAccount() {
        return parentAccount;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setTotalOdds(double totalOdds) {
        this.totalOdds = totalOdds;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Money getStake() {
        return stake;
    }

    public void setStrategy(BetStrategy strategy) {
        this.strategy = strategy;
    }

    public BetStrategy getStrategy() {
        return strategy;
    }

    public void setBonusSlip(Boolean bonusSlip) {
        this.bonusSlip = bonusSlip;
    }

    public Boolean getBonusSlip() {
        return bonusSlip;
    }

    public double getBonusOdds() {
        return bonusOdds;
    }

    public void setBonusOdds(double bonusOdds) {
        this.bonusOdds = bonusOdds;
    }

    public void setBrokerType(BrokerType brokerType) {
        this.brokerType = brokerType;
    }

    public BrokerType getBrokerType() {
        return brokerType;
    }
}
