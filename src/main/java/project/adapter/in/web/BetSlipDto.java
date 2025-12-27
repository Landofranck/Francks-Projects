package project.adapter.in.web;

import project.domain.model.Enums.BetStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BetSlipDto {
    private Long id;
    private List<MatchEventPickDto> picks;
    private BetStatus status;
    private String category;
    private Instant createdAt;
    //int the reducer method betslips will be created without parent accounts
    private BettingAccountDto parentAccount;
    private BigDecimal stake;
    private double totalOdds;

    public BetSlipDto(String category) {
        this.category = category;
        this.picks = new ArrayList<>();
        this.status = BetStatus.PENDING;
        this.totalOdds=0;
    }



    public double getTotalOdds() {
        return totalOdds;
    }

    public void setParentAccount(BettingAccountDto parentAccount) {
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

    public List<MatchEventPickDto> getPicks() {
        return picks;
    }

    public String getCategory() {
        return category;
    }

    public void setPicks(List<MatchEventPickDto> picks) {
        this.picks = picks;
    }

    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BettingAccountDto getParentAccount() {
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

    public BigDecimal getStake() {
        return stake;
    }
}
