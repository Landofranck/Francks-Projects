package project.adapter.in.web.bettinAccountDTO.betslip;

import project.adapter.in.web.MatchEventPickDto;
import project.domain.model.Enums.BetStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EmptyBetSlipDto {
    private Long id;
    private BetStatus status;
    private String category;
    private double totalOdds;
    private BigDecimal stake;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTotalOdds(double totalOdds) {
        this.totalOdds = totalOdds;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public BigDecimal getStake() {
        return stake;
    }

    public Long getId() {
        return id;
    }

    public BetStatus getStatus() {
        return status;
    }

    public double getTotalOdds() {
        return totalOdds;
    }

    public String getCategory() {
        return category;
    }
}
