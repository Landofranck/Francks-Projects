package project.domain.model;

import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStatus;

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

    public DraftBetSlip(BetCategory category) {
        this.category = category;
        this.picks = new ArrayList<>();
        this.status = BetStatus.CREATING;
        this.stake = new Money(BigDecimal.ZERO);
        this.potentialWinning = new Money(BigDecimal.ZERO);
        this.totalOdds = 0;
        this.numberOfEvents = picks.size();
    }

    public void makeTotalOdds() {
        double output = 1;
        for (MatchOutComePick m : picks) {
            output *= m.getOdd();
        }
        this.totalOdds = output;
    }

    public void calculatPotentialWinning() {
        this.potentialWinning = new Money(stake.getValue().multiply(BigDecimal.valueOf(totalOdds)));
    }

    public void addMatchEventPick(MatchOutComePick pick) {
        this.picks.add(pick);
        pick.setOwner(this);
        makeTotalOdds();
        this.numberOfEvents = picks.size();
        calculatPotentialWinning();
    }

    public void removeAllMatchEventPicks() {
        this.picks.clear();
        this.totalOdds = 0;
        setPotentialWinning(new Money(BigDecimal.ZERO));
    }

    public void setPotentialWinning(Money potentialWinning) {
        this.potentialWinning = potentialWinning;
    }

    public Money getPotentialWinning() {
        return potentialWinning;
    }

    public void removeMatchEventPicksByIndex(int i) {
        this.picks.remove(i);
        makeTotalOdds();
        this.numberOfEvents = picks.size();
        calculatPotentialWinning();
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
        calculatPotentialWinning();
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
}
