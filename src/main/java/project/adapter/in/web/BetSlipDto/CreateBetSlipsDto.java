package project.adapter.in.web.BetSlipDto;


import project.adapter.in.web.MatchDto;

import java.math.BigDecimal;
import java.util.List;

public class CreateBetSlipsDto {
    private String category;
    private BigDecimal totalStake;
    private BigDecimal bonusAmount;
    private List<MatchDto> matches;

    public CreateBetSlipsDto() {}

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getTotalStake() { return totalStake; }
    public void setTotalStake(BigDecimal totalStake) { this.totalStake = totalStake; }

    public BigDecimal getBonusAmount() { return bonusAmount; }
    public void setBonusAmount(BigDecimal bonusAmount) { this.bonusAmount = bonusAmount; }

    public List<MatchDto> getMatches() { return matches; }
    public void setMatches(List<MatchDto> matches) { this.matches = matches; }
}

