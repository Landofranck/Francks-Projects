package project.adapter.in.web.bettinAccountDTO.betslip;

import java.math.BigDecimal;
import java.util.List;

public class MakeBetRequestDto {
    private List<Long> matchIds;
    private List<String> outComes;
    private BigDecimal stake;
    private String description;//use for now as category

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
