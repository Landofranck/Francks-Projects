package project.application.port.in;

import project.domain.model.BetSlip;

public interface MakeBet {
    public BetSlip addBet(Long accountId, BetSlip betSlip);
}
