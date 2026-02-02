package project.adapter.in.web.bettinAccountDTO.betslip;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import project.domain.model.Enums.BetStrategy;

import java.math.BigDecimal;
import java.util.List;

public class MakeBetRequestDto {
    private List<Long> matchIds;
    private List<String> outComes;
    @Positive
    private BigDecimal stake;
    @NotNull
    private BetStrategy strategy;//use for now as category
    @NotNull
    private Integer bonusSlip;

    public void setMatchIds(List<Long> matchIds) {
        this.matchIds = matchIds;
    }

    public List<Long> getMatchIds() {
        return matchIds;
    }

    public void setOutComes(List<String> outComes) {
        this.outComes = outComes;
    }

    public List<String> getOutComes() {
        return outComes;
    }

    public void setStrategy(BetStrategy strategy) {
        this.strategy = strategy;
    }

    public BetStrategy getStrategy() {
        return strategy;
    }

    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }

    public BigDecimal getStake() {
        return stake;
    }

    public void setBonusSlip(Integer bonusSlip) {
        this.bonusSlip = bonusSlip;
    }

    public Integer getBonusSlip() {
        return bonusSlip;
    }
}
