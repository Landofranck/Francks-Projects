package project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip;

import jakarta.validation.constraints.NotNull;
import project.adapter.in.web.Utils.Link;

import java.util.ArrayList;
import java.util.List;

public class BetHistoryDto {
    @NotNull
    private List<ReadBetSlipDto> betHistory;
    private List<Link> links;

    public BetHistoryDto(List<ReadBetSlipDto> betHistory) {
        this.betHistory=betHistory;
        this.links=new ArrayList<>();
    }

    public List<ReadBetSlipDto> getBetHistory() {
        return betHistory;
    }

    public void setBetHistory(List<ReadBetSlipDto> betHistory) {
        this.betHistory = betHistory;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
