package project.adapter.in.web.bettinAccountDTO.betslip;

import jakarta.validation.constraints.NotNull;
import project.adapter.in.web.MatchEventPickDto;
import project.domain.model.Enums.BetStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class BetSlipDto {
    @NotNull
    private Long id;
    @NotNull
    private Long parentBettingAccountId;
    @NotNull
    private String category;
    private Instant createdAt;
    private List<MatchEventPickDto> picks;
    private BetStatus status;
    private double totalOdds;
    private BigDecimal stake;


    public BetSlipDto() {}

    public Long getParentBettingAccountId() { return parentBettingAccountId; }
    public void setParentBettingAccountId(Long parentBettingAccountId) { this.parentBettingAccountId = parentBettingAccountId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BetStatus getStatus() { return status; }
    public void setStatus(BetStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public BigDecimal getStake() { return stake; }
    public void setStake(BigDecimal stake) { this.stake = stake; }

    public double getTotalOdds() { return totalOdds; }
    public void setTotalOdds(double totalOdds) { this.totalOdds = totalOdds; }

    public List<MatchEventPickDto> getPicks() { return picks; }
    public void setPicks(List<MatchEventPickDto> picks) { this.picks = picks; }
}
