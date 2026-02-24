package project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip;

import jakarta.validation.constraints.NotNull;
import project.adapter.in.web.Utils.Link;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.ReadMatchEventPickDto;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BetStrategy;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReadBetSlipDto {
    @NotNull
    private Long id;
    @NotNull
    private Long parentBettingAccountId;
    private BetCategory category;
    private Instant createdAt;
    private List<ReadMatchEventPickDto> picks;
    private BetStatus status;
    private double totalOdds;
    private BigDecimal stake;
    private BigDecimal potentialWinning;
    private BetStrategy strategy;
    private Boolean bonusSlip;
    private double bonusOdds;

    private List<Link> links=new ArrayList<>();

    public ReadBetSlipDto() {
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

    public List<ReadMatchEventPickDto> getPicks() {
        return picks;
    }

    public void setPicks(List<ReadMatchEventPickDto> picks) {
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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> selfLinks) {
        this.links = selfLinks;
    }
}
