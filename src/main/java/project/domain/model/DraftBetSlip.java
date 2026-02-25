package project.domain.model;

import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DraftBetSlip implements Event {
    private Long id;
    private List<MatchOutComePick> picks;
    private BetStatus status;
    private BetCategory category;
    private Instant createdAt;
    //int the reducer method betslips will be created without parent accounts
    private BettingAccount draftSlipOwner;
    private Money stake;
    private double totalOdds;
    private int numberOfEvents;
    private Money potentialWinning;
    private BetStrategy strategy;
    private Boolean bonusSlip;
    private double bonusOdds;
    private BrokerType brokerType;

    public DraftBetSlip(BetCategory category) {
        this.category = category;
        this.picks = new ArrayList<>();
        this.status = BetStatus.CREATING;
        this.stake = new Money(BigDecimal.ZERO);
        this.potentialWinning = new Money(BigDecimal.ZERO);
        this.totalOdds = 0;
        this.bonusOdds = 1;
        this.bonusSlip = false;
    }

    public void placeBet(Money stake, BetStrategy strategy, Instant now, Boolean isBonus) {

        setStake(stake);
        makeTotalOdds();
        setBonusSlip(isBonus);
        calculatePotentialWinning();
        setStrategy(strategy);
        setCreatedAt(now);
    }

    public void calculatePotentialWinning() {

        if (this.brokerType == BrokerType.ONE_X_BET) {
            if (bonusSlip) {
                this.potentialWinning = new Money(stake.getValue().multiply(BigDecimal.valueOf((totalOdds / bonusOdds))));
            } else {
                this.potentialWinning = new Money(stake.getValue().multiply(BigDecimal.valueOf((totalOdds))));
            }
        }else if (brokerType == BrokerType.BET_MOMO) {
            if (bonusSlip) {
                this.potentialWinning = new Money(stake.getValue().multiply(BigDecimal.valueOf(totalOdds - 1)));
            } else {
                this.potentialWinning = new Money(stake.getValue().multiply(BigDecimal.valueOf((totalOdds))));
            }
        }
        else {
            this.potentialWinning = new Money(stake.getValue().multiply(BigDecimal.valueOf((totalOdds))));
        }


    }

    public void checkCategory() {
        if (picks.size() > 1) this.category = BetCategory.COMBINATION;
        else this.category = BetCategory.SINGLE;
    }

    public void addMatchEventPick(MatchOutComePick pick) {
        this.picks.add(pick);
        pick.setOwner(this);
        makeTotalOdds();
        this.numberOfEvents = picks.size();
        calculatePotentialWinning();
    }

    public void removeMatchEventPicksByIndex(int i) {
        this.picks.remove(i);
        makeTotalOdds();
        this.numberOfEvents = picks.size();
        calculatePotentialWinning();
    }

    public void removeAllMatchEventPicks() {
        this.picks.clear();
        this.totalOdds = 0;
        setPotentialWinning(new Money(BigDecimal.ZERO));
    }

    public void makeTotalOdds() {
        double output = 1;
        int count = 0;
        double bonusOutput = 1;
        if (this.brokerType == BrokerType.ONE_X_BET) {

            for (MatchOutComePick m : picks) {
                count++;
                output *= m.getOdd();
                if (count == 3)
                    bonusOutput = 1.03;
                if (count > 3)
                    bonusOutput += 0.01;

            }
            this.totalOdds = output * bonusOutput;
        } else if (this.brokerType == BrokerType.BET_PAWA) {
            for (MatchOutComePick m : picks) {
                if (m.getOdd() > 1.2) {
                    count++;
                }
                output *= m.getOdd();
                if (count == 3) {
                    bonusOutput = 1.03;
                }
                if (count == 4) {
                    bonusOutput = 1.05;
                }
                if (count > 4) {
                    bonusOutput += 0.05;
                }
                if (count > 20) {
                    bonusOutput += 1.1;
                }
                if (count > 29) {
                    bonusOutput += 1.15;
                }
                if (count == 28) {
                    bonusOutput = 2.85;
                }

            }

            this.totalOdds = (((100 + (output * 100) - 100) + ((output * 100) - 100) * (bonusOutput - 1)) / 100);
        } else {
            for (MatchOutComePick m : picks) {
                output *= m.getOdd();
            }
        }
        this.bonusOdds = bonusOutput;
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

    public void setDraftSlipOwner(BettingAccount draftSlipOwner) {
        this.draftSlipOwner = draftSlipOwner;
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
        calculatePotentialWinning();

    }


    public void setId(Long id) {
        this.id = id;
    }

    public BettingAccount getDraftSlipOwner() {
        return draftSlipOwner;
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
        return this.bonusOdds;
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
