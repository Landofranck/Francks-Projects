package project.adapter.in.web.bettinAccountDTO;

import project.adapter.in.web.bettinAccountDTO.betslip.BetSlipDto;

public class AddPickRequestBetSlipDto {
    private BetSlipDto slip;
    private Long matchId;
    private String outcomeName;

    public String getOutcomeName() {
        return outcomeName;
    }

    public BetSlipDto getSlip() {
        return slip;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public void setSlip(BetSlipDto slip) {
        this.slip = slip;
    }
}