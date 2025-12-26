package project.domain.model;

import project.domain.model.Enums.BetStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BetSlip {
    private Long id;
    private List<MatchEventPick> picks;
    private BetStatus status;
    private final String category;
    private final Instant createdAt;
    //int the reducer method betslips will be created without parent accounts
    private BettingAccount parentAccount;
    private Money stake;
    private double totalOdds;

    public BetSlip(String category, Money stake, Instant presentMoment) {
        this.category = category;
        this.picks = new ArrayList<>();
        this.status = BetStatus.PENDING;
        this.createdAt = presentMoment;
    }

    private void makeTotalOdds() {
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
    }

    public double getTotalOdds() {
        return totalOdds;
    }

    public void setParentAccount(BettingAccount parentAccount) {
        this.parentAccount = parentAccount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setPicks(List<MatchEventPick> picks) {
        this.picks = picks;
    }

    public void setOwner(BettingAccount bettingAccount) {
        this.parentAccount = bettingAccount;
    }

    public BettingAccount getParentAccount() {
        return parentAccount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Money getStake() {
        return stake;
    }
}
