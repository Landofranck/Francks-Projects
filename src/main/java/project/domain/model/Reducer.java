package project.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//this class should create a liste of the betslip combinations, it is also stored in the
public class Reducer implements Account {
    private Long id;
    private Money totalStake;
    private List<Match> betMatches;
    private BetSlip newSlip;
    private List<BetSlip> slips;
    private Money bonusAmount;

    public Reducer(Money stake, Money bonusAmount) {
        this.totalStake = Objects.requireNonNull(stake);
        this.bonusAmount = Objects.requireNonNull(bonusAmount);
        this.slips = new ArrayList<>();
        this.betMatches = new ArrayList<>();
    }

    public void addEventToSlip(BetSlip slip, MatchEventPick pick) {
        slip.addMatchEventPick(pick);
        slip.makeTotalOdds();
    }

    public void createBetSlip(String category) {
        Objects.requireNonNull(category);
        BetSlip newBetslip = new BetSlip(category);
        addBetSlip(newBetslip);
    }

    public void addBetSlip(BetSlip b) {
        Objects.requireNonNull(b);
        this.slips.add(b);
    }

    @Override
    public Long getAccountId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<BetSlip> getSlips() {
        return this.slips;
    }

    public List<Match> getBetMatches() {
        return betMatches;
    }

    public Money getTotalStake() {
        return totalStake;
    }

    public Money getBonusAmount() {
        return bonusAmount;
    }

    public void setBetMatches(List<Match> betMatches) {
        this.betMatches = betMatches;
    }

    public void setBonusAmount(Money bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public void setSlips(List<BetSlip> slips) {
        this.slips = slips;
    }

    public void setTotalStake(Money totalStake) {
        this.totalStake = totalStake;
    }
}
