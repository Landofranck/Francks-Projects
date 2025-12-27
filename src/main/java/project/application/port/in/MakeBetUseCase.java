package project.application.port.in;

import project.domain.model.BetSlip;

public interface MakeBetUseCase {
    public BetSlip addBet(Long accountId, BetSlip betSlip);
}
