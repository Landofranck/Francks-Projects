package project.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.util.List;

@XmlRootElement
public class ReducerDto {
    private Long id;
    @NotNull(message = "TotalStake must have a value")
    private BigDecimal totalStake;
    @NotNull(message = "betMatches must not be null")
    private List<MatchDto> betMatches;
    @NotNull(message = "slips must not be null")
    private List<BetSlipDto> slips;
    @NotNull(message = "bonus amount must not be null")
    private Money bonusAmount;







    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<BetSlipDto> getSlips() {
        return this.slips;
    }

    public List<MatchDto> getBetMatches() {
        return betMatches;
    }

    public BigDecimal getTotalStake() {
        return totalStake;
    }

    public Money getBonusAmount() {
        return bonusAmount;
    }

    public void setBetMatches(List<MatchDto> betMatches) {
        this.betMatches = betMatches;
    }

    public void setBonusAmount(Money bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public void setSlips(List<BetSlipDto> slips) {
        this.slips = slips;
    }

    public void setTotalStake(BigDecimal totalStake) {
        this.totalStake = totalStake;
    }
}
