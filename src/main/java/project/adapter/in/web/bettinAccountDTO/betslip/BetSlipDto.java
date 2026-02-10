package project.adapter.in.web.bettinAccountDTO.betslip;

import jakarta.validation.constraints.NotNull;
import project.adapter.in.web.MatchEventPickDto;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class BetSlipDto {
    @NotNull
    private Long id;
    @NotNull
    private Long parentBettingAccountId;
    private BetCategory category;
    private Instant createdAt;
    private List<MatchEventPickDto> picks;
    private BetStatus status;
    private double totalOdds;
    private BigDecimal stake;
    private BigDecimal potentialWinning;
    private BetStrategy strategy;
    private Boolean bonusSlip;
    private double bonusOdds;

    public BetSlipDto() {
    }

    public Long getParentBettingAccountId() {
        return parentBettingAccountId;
    }

    public void setParentBettingAccountId(Long parentBettingAccountId) {
        this.parentBettingAccountId = parentBettingAccountId;
    }

    public void setPotentialWinning(BigDecimal potentialWinning) {
        this.potentialWinning = potentialWinning;
    }

    public BigDecimal getPotentialWinning() {
        return potentialWinning;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BetCategory getCategory() {
        return category;
    }

    public void setCategory(BetCategory category) {
        this.category = category;
    }

    public BetStatus getStatus() {
        return status;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getStake() {
        return stake;
    }

    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }

    public double getTotalOdds() {
        return totalOdds;
    }

    public void setTotalOdds(double totalOdds) {
        this.totalOdds = totalOdds;
    }

    public List<MatchEventPickDto> getPicks() {
        return picks;
    }

    public void setPicks(List<MatchEventPickDto> picks) {
        this.picks = picks;
    }

    public BetStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(BetStrategy strategy) {
        this.strategy = strategy;
    }

    public Boolean getBonusSlip() {
        return this.bonusSlip;
    }

    public void setBonusSlip(Boolean bonusSlip) {
        this.bonusSlip = bonusSlip;
    }

    public void setBonusOdds(double bonusOdds) {
        this.bonusOdds=bonusOdds;
    }

    public double getBonusOdds() {
        return bonusOdds;
    }
}
