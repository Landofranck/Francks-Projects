package project.domain.model;

import project.domain.model.Enums.BetStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DraftBetSlip implements Event {
    private Long id;
    private List<MatchEventPick> picks;
    private BetStatus status;
    private String category;
    private Instant createdAt;
    //int the reducer method betslips will be created without parent accounts
    private BettingAccount draftSlipOwner;
    private Money stake;
    private double totalOdds;
    private int numberOfEvents;

    public DraftBetSlip(String category) {
        this.category = category;
        this.picks = new ArrayList<>();
        this.status = BetStatus.PENDING;
        this.stake= new Money(BigDecimal.ZERO);
        this.totalOdds=0;
        this.numberOfEvents=picks.size();
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
        this.numberOfEvents=picks.size();
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

    public List<MatchEventPick> getPicks() {
        return picks;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPicks(List<MatchEventPick> picks) {
        this.picks = picks;
    }

    public void setStake(Money stake) {
        this.stake = stake;
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
