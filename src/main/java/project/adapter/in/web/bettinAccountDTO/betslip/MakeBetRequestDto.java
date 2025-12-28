package project.adapter.in.web.bettinAccountDTO.betslip;

import java.math.BigDecimal;

public class MakeBetRequestDto {
    private BetSlipDto slip;
    private BigDecimal stake;
    private String description;

    public void setSlip(BetSlipDto slip) {
        this.slip = slip;
    }

    public BetSlipDto getSlip() {
        return slip;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }

    public BigDecimal getStake() {
        return stake;
    }
}
