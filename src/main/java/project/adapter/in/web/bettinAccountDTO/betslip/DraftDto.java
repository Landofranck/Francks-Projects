package project.adapter.in.web.bettinAccountDTO.betslip;

import project.adapter.in.web.MatchEventPickDto;
import project.domain.model.Enums.BetStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class DraftDto {
    private Long id;
    private Long parentBettingAccountId;
    private String category;
    private List<MatchEventPickDto> picks;
    private double totalOdds;
    private BigDecimal stake;

    public void setPicks(List<MatchEventPickDto> picks) {
        this.picks = picks;
    }

    public void setTotalOdds(double totalOdds) {
        this.totalOdds = totalOdds;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setParentBettingAccountId(Long parentBettingAccountId) {
        this.parentBettingAccountId = parentBettingAccountId;
    }

    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }
}
